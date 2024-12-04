package vedruna.webClient.Controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vedruna.webClient.DTO.ApiResponse2;
import vedruna.webClient.Service.F1ServiceImpl;
import vedruna.webClient.Service.JWTServiceImpl;
import vedruna.webClient.Service.ProductoService;

import java.util.Map;
@RestController
@RequestMapping("/f1")
@CrossOrigin
@Slf4j
public class F1Controller {

    @Autowired
    private F1ServiceImpl f1Service;

    @GetMapping(path = "/piloto/{nombre}")
    public Mono<ApiResponse2> getTeamStatistics(
            @PathVariable String nombre,
            @RequestHeader("metodoPago") String metodoPago, @RequestHeader("Authorization")String jwt)  {

        return f1Service.PilotoPorNombre(nombre,metodoPago,jwt);

    };

}
