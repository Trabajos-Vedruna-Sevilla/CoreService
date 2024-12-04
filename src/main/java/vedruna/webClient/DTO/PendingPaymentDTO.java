package vedruna.webClient.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingPaymentDTO {

    public Date f_ini;
    public Date f_fin;
    public Integer numReq;
    public Integer met_x_metpago;
    public float n_importe_peticion;
    public float n_porcentaje_descuento;

}
