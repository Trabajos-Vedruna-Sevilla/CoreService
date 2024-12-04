package vedruna.webClient.Service;

import reactor.core.publisher.Mono;
import vedruna.webClient.DTO.ApiResponse2;
import vedruna.webClient.DTO.ProductoDTO;

public interface F1ServiceI {

    Mono<ApiResponse2> PilotoPorNombre(String nombre, String metodoPago, String jwt);

}
