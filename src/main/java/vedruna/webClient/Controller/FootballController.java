package vedruna.webClient.Controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vedruna.webClient.DTO.ApiResponse2;
import vedruna.webClient.Service.F1ServiceImpl;
import vedruna.webClient.Service.FootballServiceI;
import vedruna.webClient.Service.FootballServiceImpl;
import vedruna.webClient.Service.JWTServiceImpl;

import java.util.Map;
@RestController
@RequestMapping("/futbol")
@CrossOrigin
@Slf4j
public class FootballController {

    @Autowired
    private FootballServiceImpl footballService;
    @Value("${api.key}")
    private String apiKey;
    @Autowired
    private JWTServiceImpl jwtService;

    @GetMapping(path = "/statistics/{season}/{team}/{league}")
    public Mono<ApiResponse2> getTeamStatistics(
            @PathVariable String season,
            @PathVariable String team,
            @PathVariable String league,
            @RequestHeader("metodoPago") String metodoPago, @RequestHeader("Authorization")String jwt) {

        return footballService.infoEquiposPorLigas(season ,team ,league,metodoPago,jwt);

    };


    @GetMapping(path = "/jugador/{id}")
    public Mono<ApiResponse2> getTeamStatistics(
            @PathVariable int id,
            @RequestHeader("metodoPago") String metodoPago, @RequestHeader("Authorization")String jwt) {

        return footballService.infoJugador(id,jwt, metodoPago);

    };
}
