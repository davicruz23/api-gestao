package tads.ufrn.apigestao.domain.dto.cep;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CepResponse {
    private String cep;
    private String street;
    private String city;
}
