package vedruna.webClient.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;


@Service
public class BasketBallServiceImpl implements BasketBallServiceI{



    public String getNbaSeason() {
        // URL del endpoint
        String endpoint = "https://v2.nba.api-sports.io/seasons";
        String apiKey = "29d12c44d0bdf082d4a9c3c632664cb5";

        // Crear un encabezado con la clave API
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-rapidapi-key", apiKey);
        headers.add("x-rapidapi-host", "v2.nba.api-sports.io");
        headers.add("useQueryString", "true");

        // Crear un objeto HttpEntity con el encabezado
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Crear un RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Realizar la solicitud GET y obtener la respuesta
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                entity, // Pasar la entidad que contiene el encabezado
                String.class
        );

        // Devolver el cuerpo de la respuesta
        return responseEntity.getBody();
    }
    public String getNbaLeagues() {
        // URL del endpoint
        String endpoint = "https://v2.nba.api-sports.io/leagues";
        String apiKey = "29d12c44d0bdf082d4a9c3c632664cb5";

        // Crear un encabezado con la clave API
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-rapidapi-key", apiKey);
        headers.add("x-rapidapi-host", "v2.nba.api-sports.io");
        headers.add("useQueryString", "true");

        // Crear un objeto HttpEntity con el encabezado
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Crear un RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Realizar la solicitud GET y obtener la respuesta
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                entity, // Pasar la entidad que contiene el encabezado
                String.class
        );

        // Devolver el cuerpo de la respuesta
        return responseEntity.getBody();
    }

    public String getNbaGames(String year, String month, String day) {
        // URL del endpoint
        String endpoint = "https://v2.nba.api-sports.io/games?date=" + year + "-" + month + "-" + day;
        String apiKey = "29d12c44d0bdf082d4a9c3c632664cb5";

        // Crear un encabezado con la clave API
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-rapidapi-key", apiKey);
        headers.add("x-rapidapi-host", "v2.nba.api-sports.io");
        headers.add("useQueryString", "true");

        // Crear un objeto HttpEntity con el encabezado
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Crear un RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Realizar la solicitud GET y obtener la respuesta
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                entity, // Pasar la entidad que contiene el encabezado
                String.class
        );

        // Devolver el cuerpo de la respuesta
        return responseEntity.getBody();
    }

    public String getNbaTeamStats(String season, String id) {
        // URL del endpoint
        String endpoint = "https://v2.nba.api-sports.io/teams/statistics?season=" + season +"&id=" + id ;
        String apiKey = "29d12c44d0bdf082d4a9c3c632664cb5";

        // Crear un encabezado con la clave API
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-rapidapi-key", apiKey);
        headers.add("x-rapidapi-host", "v2.nba.api-sports.io");
        headers.add("useQueryString", "true");

        // Crear un objeto HttpEntity con el encabezado
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Crear un RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Realizar la solicitud GET y obtener la respuesta
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                entity, // Pasar la entidad que contiene el encabezado
                String.class
        );

        // Devolver el cuerpo de la respuesta
        return responseEntity.getBody();
    }

    public String getNbaTeams(String id) {
        // URL del endpoint
        String endpoint = "https://v2.nba.api-sports.io/teams" + "?id=" + id ;
        String apiKey = "29d12c44d0bdf082d4a9c3c632664cb5";

        // Crear un encabezado con la clave API
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-rapidapi-key", apiKey);
        headers.add("x-rapidapi-host", "v2.nba.api-sports.io");
        headers.add("useQueryString", "true");

        // Crear un objeto HttpEntity con el encabezado
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Crear un RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Realizar la solicitud GET y obtener la respuesta
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                entity, // Pasar la entidad que contiene el encabezado
                String.class
        );

        // Devolver el cuerpo de la respuesta
        return responseEntity.getBody();
    }

    public String getNbaPlayers(String id) {
        // URL del endpoint
        String endpoint = "https://v2.nba.api-sports.io/players" + "?id=" + id ;
        String apiKey = "29d12c44d0bdf082d4a9c3c632664cb5";

        // Crear un encabezado con la clave API
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-rapidapi-key", apiKey);
        headers.add("x-rapidapi-host", "v2.nba.api-sports.io");
        headers.add("useQueryString", "true");

        // Crear un objeto HttpEntity con el encabezado
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Crear un RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Realizar la solicitud GET y obtener la respuesta
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                entity, // Pasar la entidad que contiene el encabezado
                String.class
        );

        // Devolver el cuerpo de la respuesta
        return responseEntity.getBody();
    }

}
