package vedruna.webClient.Controller;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vedruna.webClient.DTO.*;
import vedruna.webClient.Service.JWTServiceImpl;
import vedruna.webClient.Service.ProductoService;

//jwt
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//jwt
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
@Slf4j
public class MainController {

    @Autowired
    private ProductoService productoService;
    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private JWTServiceImpl jwtService;

    @GetMapping(path = "/products")
    public ProductoDTO[] getProducts() {
        return productoService.searchAllProducts();
    }

    @GetMapping(path = "/prueba")
    public String getProductsPrueba() {
        return productoService.searchAllProductsPrueba();
    }

    @GetMapping(path = "/pablito")
    public ResponseEntity getP() {
        return WebClient.create("http://localhost:8081/").get().uri("api/all").retrieve().bodyToMono(ResponseEntity.class).block();
    }

    @GetMapping(path = "/a")
    /*Mono<ApiResponse> ||  ApiResponse*/
    public Mono<ApiResponse> getA() {
        Logger log = LoggerFactory.getLogger(MainController.class);
       WebClient webClient = WebClient.builder()
                .baseUrl("https://v3.football.api-sports.io")
                .defaultHeader("x-rapidapi-key", "29d12c44d0bdf082d4a9c3c632664cb5")
                .build();
        WebClient webClient2 = WebClient.builder()
                .baseUrl("https://v3.football.api-sports.io")
                .defaultHeader("x-rapidapi-key", "29d12c44d0bdf082d4a9c3c632664cb5")
                .build();
        log.debug("Web client build");
        return webClient.get()
                .uri("/countries")
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        log.debug("OK status response");
                        ApiResponse re = new ApiResponse();
                        HttpStatusCode status = response.statusCode();
                        log.debug("Status".concat(status.toString()));
                        ClientResponse.Headers headers = response.headers();
                        HttpHeaders requestHeaders = response.request().getHeaders();
                        Mono<Response> responseMono = response.bodyToMono(Response.class);
                        responseMono.subscribe(responsePayload -> {
                            re.setResponsePayload(responsePayload);
                            re.setStatus(status.value());
                            //re.setRequestPayload(headers.header("x-ratelimit-requests-remaining"));
                            re.setRequestHeaders(requestHeaders);
                            re.setRequestPayload(headers.asHttpHeaders().toSingleValueMap());
                        });
                        log.debug("Inserted data".concat(re.toString()));

                        return Mono.just(re);
                    } else {
                        log.debug("ERROR status response");
                        return response.createError();
                    }

                });
    }

    @GetMapping(path = "/a2")
    /*Mono<ApiResponse> ||  ApiResponse*/
    public Mono<ApiResponse> getA2() {
        Logger log = LoggerFactory.getLogger(MainController.class);
        WebClient webClient = WebClient.builder()
                .baseUrl("https://v3.football.api-sports.io")
                .defaultHeader("x-rapidapi-key", "29d12c44d0bdf082d4a9c3c632664cb5")
                .build();

        log.debug("Web client build");

        ApiResponse re = new ApiResponse();  // Crear instancia de ApiResponse

        return webClient.get()
                .uri("/countries")
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    if (status.is2xxSuccessful()) {
                        log.debug("OK status response");

                        ClientResponse.Headers headers = response.headers();
                        HttpHeaders requestHeaders = response.request().getHeaders();
                        Mono<Response> responseMono = response.bodyToMono(Response.class);

                        return responseMono.doOnSuccess(responsePayload -> {
                            log.debug("Setting response data");
                            re.setResponsePayload(responsePayload);
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
                .thenReturn(re);
    }


    @GetMapping(path = "/countries")
    /*Mono<ApiResponse> ||  ApiResponse*/
    public Mono<ApiResponse2> getB2(@RequestHeader("metodoPago") String metodoPago,
                                    @RequestHeader("Authorization") String jwt) {
        Logger log = LoggerFactory.getLogger(MainController.class);
        WebClient webClient = WebClient.builder()
                .baseUrl("https://v3.football.api-sports.io")
                .defaultHeader("x-rapidapi-key", apiKey)
                .build();

        WebClient webClientAudit = WebClient.builder()
                        .baseUrl("http://localhost:8081/api")
                        .build();

        log.debug("Web client build");

        ApiResponse2 re = new ApiResponse2();  // Crear instancia de ApiResponse

        return webClient.get()
                .uri("/countries")
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    if (status.is2xxSuccessful()) {
                        log.debug("OK status response");

                        ClientResponse.Headers headers = response.headers();
                        HttpHeaders requestHeaders = response.request().getHeaders();
                        Mono<Map<String, Object>> responseMono = response.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});

                        return responseMono.doOnSuccess(responseBody -> {
                            log.debug("Setting response data");
                            re.setUserId(jwtService.getId(jwt));
                            Calendar calendar = Calendar.getInstance();

                            // Formatear la fecha
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String fechaFormateada = sdf.format(calendar.getTime());
                            Date fecha;
                            try {
                                fecha = sdf.parse(fechaFormateada);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            re.setRequestDate(fecha);
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
                .flatMap(response -> {
                     return webClientAudit.post()
                            .uri("/insertar")
                            .body(Mono.just(re), ApiResponse2.class)
                            .retrieve()
                            .bodyToMono(ApiResponse2.class);
                })
                .thenReturn(re);
    }
    /*return webClient.get()
                .uri("/countries")
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .block();
         */

    @GetMapping(path = "/b")
    public Response getB() {
       WebClient webClient = WebClient.builder()
                .baseUrl("https://v3.football.api-sports.io")
                .defaultHeader("x-rapidapi-key", "29d12c44d0bdf082d4a9c3c632664cb5")
                .build();

        return webClient.get()
                .uri("/countries")
                .retrieve()
                .bodyToMono(Response.class)
                .block();
    }

    @GetMapping(path = "/c")
    public Response getC() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://v3.football.api-sports.io")
                .defaultHeader("x-rapidapi-key", "29d12c44d0bdf082d4a9c3c632664cb5")
                .build();

        return webClient.get()
                .uri("/countries")
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {

                        return response.bodyToMono(Response.class);
                    } else {
                        // Turn to error
                        return response.createError();
                    }

                }).block();
    }
    @GetMapping(path = "/statistics/{season}/{team}/{league}")
    public Mono<ApiResponse2> getTeamStatistics(
            @PathVariable String season,
            @PathVariable String team,
            @PathVariable String league,
            @RequestHeader("metodoPago") String metodoPago) {

        Logger log = LoggerFactory.getLogger(MainController.class);
        WebClient webClient = WebClient.builder()
                .baseUrl("https://v3.football.api-sports.io")
                .defaultHeader("x-rapidapi-key", "29d12c44d0bdf082d4a9c3c632664cb5")
                .build();

        WebClient webClientAudit = WebClient.builder()
                .baseUrl("http://localhost:8081/api")
                .build();

        ApiResponse2 re = new ApiResponse2();  // Crear instancia de ApiResponse

        log.debug("Iniciando solicitud a la API externa...");

        return webClient.get()
                .uri("/teams/statistics?season={season}&team={team}&league={league}", season, team, league)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    if (status.is2xxSuccessful()) {
                        log.debug("Solicitud a la API externa exitosa. Procesando respuesta...");

                        ClientResponse.Headers headers = response.headers();
                        HttpHeaders requestHeaders = response.request().getHeaders();
                        Mono<Map<String, Object>> responseMono = response.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});

                        return responseMono.doOnSuccess(responseBody -> {
                            log.debug("Respuesta procesada con éxito. Construyendo ApiResponse2...");

                            re.setUserId("Username");
                            re.setResponsePayload(responseBody);
                            re.setPaymentMethod(Integer.parseInt(metodoPago));  // Asignar el valor del encabezado a paymentMethod
                            re.setStatus(status.value());
                            re.setRequestHeaders(requestHeaders);
                            re.setRequestPayload(headers.asHttpHeaders().toSingleValueMap());

                            log.debug("ApiResponse2 construido con éxito: " + re.toString());
                        });
                    } else {
                        log.debug("Error en la solicitud a la API externa. Estado: " + status.toString());

                        // Puedes lanzar una excepción o devolver un Mono.error
                        return Mono.error(new RuntimeException("Error en la solicitud externa. Estado: " + status.toString()));
                    }
                })
                .doOnSuccess(success -> log.debug("Operación exitosa después de la API externa: " + success))
                .doOnError(error -> log.debug("Error después de la API externa: " + error))
                .flatMap(response -> {
                    log.debug("Iniciando solicitud de auditoría...");

                    return webClientAudit.post()
                            .uri("/insertar")
                            .body(Mono.just(re), ApiResponse2.class)
                            .retrieve()
                            .bodyToMono(ApiResponse2.class)
                            .doOnSuccess(auditSuccess -> log.debug("Operación exitosa de auditoría: " + auditSuccess))
                            .doOnError(auditError -> log.debug("Error en la operación de auditoría: " + auditError));
                }).thenReturn(re);
    }

    // jwt
    public static String decodeJwtId(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            Base64.Decoder decoder = Base64.getDecoder();
            String[] array = token.split("\\.");

            if (array.length >= 2) {
                String decodedJson = new String(decoder.decode(array[1]), StandardCharsets.UTF_8);
                try {
                    // Parsear el JSON y extraer el campo "sub" (ID)
                    JsonNode jsonNode = new ObjectMapper().readTree(decodedJson);
                    return jsonNode.get("sub").asText();
                } catch (Exception e) {
                    throw new RuntimeException("Error al parsear el token JWT", e);
                }
            } else {
                throw new RuntimeException("Token JWT no tiene el formato esperado");
            }
        } else {
            throw new RuntimeException("No se encontró el token en el encabezado");
        }
    }


}
