package vedruna.webClient.Controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import vedruna.webClient.DTO.ApiResponse2;
import vedruna.webClient.DTO.PendingPaymentDTO;
import vedruna.webClient.Service.JWTServiceImpl;
import vedruna.webClient.Service.PaymentServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/pay")
@CrossOrigin
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentServiceImpl paymentService;
    @Autowired
    private JWTServiceImpl jwtService;

    @GetMapping(path = "/request")
    /*Mono<ApiResponse> ||  ApiResponse*/
    public ApiResponse2 payRequestByUserIdAndPaymentMethod(@RequestHeader("metodoPago") String paymentMethod,
                                    @RequestHeader("Authorization") String jwt) {
        Logger log = LoggerFactory.getLogger(MainController.class);
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8081/pay")
                .build();

        log.debug("Web client build");

        ApiResponse2 re = new ApiResponse2();  // Crear instancia de ApiResponse

        webClient.put()
        .uri("/pay/"+jwtService.getId(jwt)+"/"+paymentMethod).retrieve()
                .bodyToMono(String.class)
                .subscribe(responseBody -> {
                    System.out.println("Respuesta del servidor: " + responseBody);
                });
        return re;
    }


    @GetMapping(path = "/request2")
    /*Mono<ApiResponse> ||  ApiResponse*/
    public ApiResponse2 payRequestByUserIdAndPaymentMethodBien(@RequestHeader("metodoPago") String paymentMethod,
                                                           @RequestHeader("Authorization") String jwt) {
        Logger log = LoggerFactory.getLogger(MainController.class);
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8081/pay")
                .build();

        WebClient webClient2 = WebClient.builder()
                .baseUrl("http://localhost:8081/pay")
                .build();

        log.debug("Web client build");

        ApiResponse2 re = new ApiResponse2();  // Crear instancia de ApiResponse

        webClient.put()
                .uri("/pay/"+jwtService.getId(jwt)+"/"+paymentMethod).retrieve()
                .bodyToMono(String.class)
                .subscribe(responseBody -> {
                    System.out.println("Respuesta del servidor: " + responseBody);
                });
        return re;
    }


    /*@GetMapping(path = "/pendingPeriod")
    public PendingPaymentDTO[] getPendingPeriods() {
        return paymentService.getPeriodsByPending();
    }*/

    /*@GetMapping(path = "/pendingTotalNReq")
    public Integer getPendingTotalNReq(@RequestHeader("metodoPago") String paymentMethod,
                                         @RequestHeader("Authorization") String jwt) {
        Date dateS;
        Date dateE;
        return paymentService.getNumPendingByPeriod(jwtService.getId(jwt), Integer.parseInt(paymentMethod), Date dateS, Date dateE);
    }*/

    @GetMapping(path = "/pendingTotal")
    public List<PendingPaymentDTO> getPendingTotal(@RequestHeader("metodoPago") String paymentMethod,
                                   @RequestHeader("Authorization") String jwt) {
        return paymentService.getAllPendingPayment(jwt, Integer.parseInt(paymentMethod));
    }
    @GetMapping(path = "/pendingPrice")
    public double getPendingPrice(@RequestBody List<PendingPaymentDTO> fullBill) {
        return paymentService.getAllPendingPrice(fullBill);
    }

}
