package tads.ufrn.apigestao.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.Charging;

import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.Inspector;
import tads.ufrn.apigestao.domain.Seller;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String name;
    private String cpf;
    private String function;
    private String password;
    private Seller seller;
    private Inspector inspector;
    private Collector collector;
}
