package vedruna.webClient.Service;

import reactor.core.publisher.Mono;
import vedruna.webClient.DTO.ApiResponse2;

public interface FootballServiceI {

    Mono<ApiResponse2> infoEquiposPorLigas(String season,String team, String league, String metodoPago, String jwt);
    Mono<ApiResponse2> infoJugador(int id, String jwt, String metodoPago);


}
