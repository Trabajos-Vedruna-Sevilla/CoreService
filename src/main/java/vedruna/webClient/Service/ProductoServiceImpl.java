package vedruna.webClient.Service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;


import reactor.core.publisher.Mono;
import vedruna.webClient.Controller.MainController;
import vedruna.webClient.DTO.ApiResponse2;
import vedruna.webClient.DTO.ProductoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import vedruna.webClient.DTO.PruebaDTO;

import java.util.Map;


@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    private WebClient client = WebClient.create("https://fakestoreapi.com");
    private WebClient client2 = WebClient.create("http://localhost:8081/api/insertar");


    @Override
    public ProductoDTO[] searchAllProducts() {
        return new ProductoDTO[0];
    }

    @Override
    public String searchAllProductsPrueba() {
    	Logger log = LoggerFactory.getLogger(WebClient.class);
		log.info("WebClient hace la request a API publica");
        ProductoDTO[] ps = client.get().uri("/products").retrieve().bodyToMono(ProductoDTO[].class).block();
        String r = client2.post().contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ps).retrieve().bodyToMono(String.class).block();
        log.info("WebClient postea a auditori");
        return r;
    }

    @Override
    public PruebaDTO pruebaDTO() {
        PruebaDTO a = new PruebaDTO("hola");
        client2.post().contentType(MediaType.APPLICATION_JSON).bodyValue(a).retrieve().bodyToMono(PruebaDTO.class).block();
        return new PruebaDTO("hola");
    }

    @Override
    public Mono<ApiResponse2> ejeploServicio() {
        Logger log = LoggerFactory.getLogger(MainController.class);

        // Crear webclients Api publica y auditoria
        WebClient webClient = WebClient.builder()
                .baseUrl("https://v3.football.api-sports.io")
                .defaultHeader("x-rapidapi-key", "29d12c44d0bdf082d4a9c3c632664cb5")
                .build();

        WebClient webClientAudit = WebClient.builder()
                .baseUrl("http://localhost:8081/api")
                .build();

        log.debug("Web client build");

        ApiResponse2 re = new ApiResponse2();  // Crear instancia de ApiResponse

        // returnamos el resultado de consumir el endpoind de la api publica
        return webClient.get()
                // especificamos el endpoint al que haremos la request
                .uri("/countries")
                //Funcion lamda
                .exchangeToMono(response -> {
                    // Con el codigo de estado (2xx, 3xx, 4xx, 5xx) comprobamos si ha sido exitosa la peticion
                    HttpStatusCode status = response.statusCode();

                    if (status.is2xxSuccessful()) {
                        log.debug("OK status response");

                        ClientResponse.Headers headers = response.headers();
                        HttpHeaders requestHeaders = response.request().getHeaders();
                        Mono<Map<String, Object>> responseMono = response.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});

                        return responseMono.doOnSuccess(responseBody -> {
                            log.debug("Setting response data");
                            re.setUserId("JuakyPito");
                            re.setResponsePayload(responseBody);
                            re.setStatus(status.value());
                            re.setRequestHeaders(requestHeaders);
                            re.setRequestPayload(headers.asHttpHeaders().toSingleValueMap());

                            log.debug("Inserted data: " + re.toString());
                        });
                    } else {
                        log.debug("ERROR status response");
                        re.setStatus(status.value());

                        // Agregar más información según tus necesidades
                        log.debug("Error details: " + status.toString());

                        return Mono.empty();
                    }
                })
                .doOnSuccess(success -> log.debug("Success: " + success))
                .doOnError(error -> log.debug("Error: " + error))
                // Insertamos la respuesta en auditoria
                .flatMap(response -> {
                    return webClientAudit.post()
                            .uri("/insertar")
                            .body(Mono.just(re), ApiResponse2.class)
                            .retrieve()
                            .bodyToMono(ApiResponse2.class);
                })
                .thenReturn(re);
    }


}
