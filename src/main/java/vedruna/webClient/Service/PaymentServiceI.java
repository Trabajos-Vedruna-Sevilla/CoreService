package vedruna.webClient.Service;

import reactor.core.publisher.Mono;
import vedruna.webClient.DTO.PendingPaymentDTO;

import java.util.Date;
import java.util.List;

public interface PaymentServiceI {
    Integer getNumPendingByPeriod(String userId, int paymentMethod, Date dateS, Date dateE);
    PendingPaymentDTO[] getPeriodsByPending();
    PendingPaymentDTO getPendingPayment(Date dateE, int paymentMet, int numReq);
    List<PendingPaymentDTO> getAllPendingPayment(String jwt, int paymentMet);
    double getAllPendingPrice(List<PendingPaymentDTO> fullBill);
}
