package vedruna.webClient.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public interface BasketBallServiceI {


    public String getNbaSeason();


    public String getNbaLeagues();
    public String getNbaGames(String year, String month, String day);

    public String getNbaTeamStats(String season, String id);

    public String getNbaTeams(String id);

    public String getNbaPlayers(String id);
}
