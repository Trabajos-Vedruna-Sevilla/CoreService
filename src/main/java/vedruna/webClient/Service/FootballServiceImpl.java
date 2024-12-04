package vedruna.webClient.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vedruna.webClient.Controller.MainController;
import vedruna.webClient.DTO.ApiResponse2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
@Service
public class FootballServiceImpl implements FootballServiceI {

    @Value("${api.key}")
    private String apiKey;
    @Value("${api.audit}")
    private String apiAudit;
    @Value("${api.football}")
    private String apiUrl;
    @Value("${api.football.teams}")
    private String apiUrlTeams;
    @Value("${api.football.players}")
    private String apiUrlPlayers;
    @Autowired
    private JWTServiceImpl jwtService;
    @Override
    public Mono<ApiResponse2> infoEquiposPorLigas(String season,String team, String league, String metodoPago, String jwt) {
        Logger log = LoggerFactory.getLogger(MainController.class);
        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("x-rapidapi-key", apiKey)
                .build();

        WebClient webClientAudit = WebClient.builder()
                .baseUrl(apiAudit)
                .build();

        ApiResponse2 re = new ApiResponse2();  // Crear instancia de ApiResponse

        log.debug("Iniciando solicitud a la API externa...");

        return webClient.get()
                .uri(apiUrlTeams, season, team, league)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    if (status.is2xxSuccessful()) {
                        log.debug("Solicitud a la API externa exitosa. Procesando respuesta...");

                        ClientResponse.Headers headers = response.headers();
                        HttpHeaders requestHeaders = response.request().getHeaders();
                        Mono<Map<String, Object>> responseMono = response.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});

                        return responseMono.doOnSuccess(responseBody -> {
                            log.debug("Respuesta procesada con éxito. Construyendo ApiResponse2...");

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









    @Override
    public Mono<ApiResponse2> infoJugador(int id, String jwt, String metodoPago) {
        Logger log = LoggerFactory.getLogger(MainController.class);
        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("x-rapidapi-key", apiKey)
                .build();

        WebClient webClientAudit = WebClient.builder()
                .baseUrl(apiAudit)
                .build();

        ApiResponse2 re = new ApiResponse2();  // Crear instancia de ApiResponse

        log.debug("Iniciando solicitud a la API externa...");

        return webClient.get()
                .uri(apiUrlPlayers, id)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    if (status.is2xxSuccessful()) {
                        log.debug("Solicitud a la API externa exitosa. Procesando respuesta...");

                        ClientResponse.Headers headers = response.headers();
                        HttpHeaders requestHeaders = response.request().getHeaders();
                        Mono<Map<String, Object>> responseMono = response.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});

                        return responseMono.doOnSuccess(responseBody -> {
                            log.debug("Respuesta procesada con éxito. Construyendo ApiResponse2...");

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
}
