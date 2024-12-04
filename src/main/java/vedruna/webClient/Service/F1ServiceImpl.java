package vedruna.webClient.Service;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class F1ServiceImpl implements F1ServiceI {

    @Value("${api.key}")
    private String apiKey;
    @Value("${api.audit}")
    private String apiAudit;
    @Value("${api.f1}")
    private String apiUrl;
    @Value("${api.f1.pilots}")
    private String apiUrlPilots;
    @Autowired
    private JWTServiceImpl jwtService;
    @Override
    public Mono<ApiResponse2> PilotoPorNombre(String nombre,String metodoPago, String jwt) {
        Logger log = LoggerFactory.getLogger(MainController.class);

        log.info("API Key: " + apiKey);

        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("x-rapidapi-key","29d12c44d0bdf082d4a9c3c632664cb5" )
                .build();

        WebClient webClientAudit = WebClient.builder()
                .baseUrl(apiAudit)
                .build();

        ApiResponse2 re = new ApiResponse2();  // Crear instancia de ApiResponse

        log.info("Iniciando solicitud a la API externa...");

        return webClient.get()
                .uri(apiUrlPilots, nombre)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    if (status.is2xxSuccessful()) {
                        log.info("Solicitud a la API externa exitosa. Procesando respuesta...");

                        ClientResponse.Headers headers = response.headers();
                        HttpHeaders requestHeaders = response.request().getHeaders();
                        Mono<Map<String, Object>> responseMono = response.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});

                        return responseMono.doOnSuccess(responseBody -> {
                            log.info("Respuesta procesada con éxito. Construyendo ApiResponse2...");

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
                            re.setPaymentMethod(Integer.parseInt(metodoPago));  // Asignar el valor del encabezado a paymentMethod
                            re.setStatus(status.value());
                            re.setRequestHeaders(requestHeaders);
                            re.setRequestPayload(headers.asHttpHeaders().toSingleValueMap());

                            log.info("ApiResponse2 construido con éxito: " + re.toString());
                        });
                    } else {
                        log.info("Error en la solicitud a la API externa. Estado: " + status.toString());

                        // Puedes lanzar una excepción o devolver un Mono.error
                        return Mono.error(new RuntimeException("Error en la solicitud externa. Estado: " + status.toString()));
                    }
                })
                .doOnSuccess(success -> log.info("Operación exitosa después de la API externa: " + success))
                .doOnError(error -> log.info("Error después de la API externa: " + error))
                .flatMap(response -> {
                    log.info("Iniciando solicitud de auditoría...");

                    return webClientAudit.post()
                            .uri("/insertar")
                            .body(Mono.just(re), ApiResponse2.class)
                            .retrieve()
                            .bodyToMono(ApiResponse2.class)
                            .doOnSuccess(auditSuccess -> log.info("Operación exitosa de auditoría: " + auditSuccess))
                            .doOnError(auditError -> log.info("Error en la operación de auditoría: " + auditError));
                }).thenReturn(re);
    }
}
