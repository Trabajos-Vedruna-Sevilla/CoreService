package vedruna.webClient.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vedruna.webClient.DTO.ApiResponse2;
import vedruna.webClient.DTO.PendingPaymentDTO;

import javax.print.attribute.standard.MediaSize;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentServiceI{
    @Autowired
    private JWTServiceImpl jwtService;
    @Override
    public PendingPaymentDTO[] getPeriodsByPending() {
        WebClient audit = WebClient.builder()
                .baseUrl("http://localhost:8081")
                .build();

        WebClient biller = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();

        log.debug("Web client build");

        ApiResponse2 re = new ApiResponse2();  // Crear instancia de ApiResponse

        PendingPaymentDTO[] res = biller.get()
                .uri("/getPeriodsAvailable")
                .retrieve()
                .bodyToMono(PendingPaymentDTO[].class)
                .block();

        return res;
    }

    @Override
    public Integer getNumPendingByPeriod(String userId, int paymentMethod, Date dateS, Date dateE) {
        WebClient audit = WebClient.builder()
                .baseUrl("http://localhost:8081")
                .build();

        WebClient biller = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();

        log.debug("Web client build");

        ApiResponse2 re = new ApiResponse2();  // Crear instancia de ApiResponse


        // Dates
        Date date = dateS;
        Date date2 = dateE;

        // Date Format
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        // Create a SimpleDateFormat object
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        SimpleDateFormat formatter2 = new SimpleDateFormat(format);

        // Convert Date to String
        String formattedDate = formatter.format(date);
        String formattedDate2 = formatter2.format(date2);

            log.info("Getting Num Req...");
            log.info("Formated Date = " + formattedDate);

        Integer res = audit.get()
                .uri("/pay/numPendingByDate/{userId}/{paymentMethod}",userId, paymentMethod)
                .header("dateS", formattedDate)
                .header("dateE", formattedDate2)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
            log.info("Num req = " + res);

        return res;

    }

    //int numReq = getNumPendingByPeriod(String userId, int paymentMethod, Date dateS, Date dateE)
    @Override
    public PendingPaymentDTO getPendingPayment(Date dateE, int paymentMet, int numReq) {
        WebClient audit = WebClient.builder()
                .baseUrl("http://localhost:8081")
                .build();

        WebClient biller = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();

        Date date = dateE;

        // Date Format
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        // Create a SimpleDateFormat object
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        // Convert Date to String
        String formattedDate = formatter.format(date);
        log.info(formattedDate);

        log.debug("Web client build");

        ApiResponse2 re = new ApiResponse2();  // Crear instancia de ApiResponse
            log.info("Getting Pending PaymentDTO...");

        PendingPaymentDTO res = biller.get()
                .uri("/getPricesAndDiscountsAvailableByPeriods/{paymentMethod}/{resquestNum}", paymentMet, numReq)
                .header("periodDate", formattedDate)
                .retrieve()
                .bodyToMono(PendingPaymentDTO.class)
                .block();

        res.setNumReq(numReq);
        log.info("DTO = " + res);

        return res;
    }

    @Override
    public List<PendingPaymentDTO> getAllPendingPayment(String jwt, int paymentMet) {
        List<PendingPaymentDTO> payments = new ArrayList<>();
        PendingPaymentDTO[] periods = getPeriodsByPending();
        //foreach period -> getPendingPayment(numReq)
        for (PendingPaymentDTO per: periods) {
            Date dateE = per.f_fin;
            log.info("fecha= " + dateE.toString());
            int numReq = getNumPendingByPeriod(jwtService.getId(jwt), paymentMet, per.f_ini, dateE);
            if (numReq > 0) {
                PendingPaymentDTO payment = getPendingPayment(dateE, paymentMet, numReq);
                payment.setF_ini(per.f_ini);
                payment.setF_fin(per.f_fin);
                payments.add(payment);
            }

        }
        return payments;
    }

    @Override
    public double getAllPendingPrice(List<PendingPaymentDTO> fullBill) {
        double totalPrice = 0;
        for (PendingPaymentDTO bill: fullBill) {
            int req = bill.numReq;
            float price = bill.n_importe_peticion;
            float disc = bill.n_porcentaje_descuento;
            totalPrice += ((100 - disc)/100) * price * req;
        }
        return totalPrice;
    }


}
