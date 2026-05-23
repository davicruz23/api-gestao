package tads.ufrn.apigestao.domain.dto.cep;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesDTO {

    private BigDecimal latitude;
    private BigDecimal longitude;

}