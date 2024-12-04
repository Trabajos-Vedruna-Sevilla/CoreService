package vedruna.webClient.Service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vedruna.webClient.DTO.ApiResponse2;
import vedruna.webClient.DTO.ProductoDTO;
import vedruna.webClient.DTO.PruebaDTO;

public interface ProductoService {

    ProductoDTO[] searchAllProducts();

    String searchAllProductsPrueba();


    PruebaDTO pruebaDTO();

    Mono<ApiResponse2> ejeploServicio();
}
