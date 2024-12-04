package vedruna.webClient.Controller;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vedruna.webClient.DTO.*;
import vedruna.webClient.Service.BasketBallServiceI;
import vedruna.webClient.Service.BasketBallServiceImpl;
import vedruna.webClient.Service.ProductoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nba")
@CrossOrigin
@Slf4j
public class BasketController {

    private BasketBallServiceImpl basketBallService;

    @Autowired
    public BasketController(BasketBallServiceImpl basketBallService) {
        this.basketBallService = basketBallService;
    }

    @GetMapping(path = "/seasons")
    public String getNbaSeason() {
        return basketBallService.getNbaSeason();
    }

    @GetMapping(path = "/leagues")
    public String getNbaLeagues() {
        return basketBallService.getNbaLeagues();
    }

    @GetMapping(path = "/games/{year}/{month}/{day}")
    public String getNbaGames(@PathVariable String year,@PathVariable String month, @PathVariable String day) {
        return basketBallService.getNbaGames(year,month,day);
    }

    @GetMapping(path = "/teamStats/{season}/{id}")
    public String getNbaTeamStatistics(@PathVariable String season,@PathVariable String id) {
        return basketBallService.getNbaTeamStats(season,id);
    }

    @GetMapping(path = "/teams/{id}")
    public String getNbaTeams(@PathVariable String id) {
        return basketBallService.getNbaTeams(id);
    }
    @GetMapping(path = "/players/{id}")
    public String getNbaPlayers(@PathVariable String id) {
        return basketBallService.getNbaPlayers(id);
    }
}
