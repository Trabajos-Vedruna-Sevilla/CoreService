package vedruna.webClient.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Slf4j
public class JWTServiceImpl implements JWTServiceI{
    @Override
    public String getId(String jwt) {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);

            Base64.Decoder decoder = Base64.getDecoder();
            String[] array = jwt.split("\\.");

            if (array.length >= 2) {
                String decodedJson = new String(decoder.decode(array[1]), StandardCharsets.UTF_8);
                try {
                    JsonNode jsonNode = new ObjectMapper().readTree(decodedJson);
                    return jsonNode.get("sub").asText();
                } catch (Exception e) {
                    throw new RuntimeException("Error al parsear el JWT", e);
                }
            } else {
                throw new RuntimeException("Token JWT no tiene el formato esperado");
            }
        } else {
            throw new RuntimeException("No se encontró el token");
        }
    }
}
