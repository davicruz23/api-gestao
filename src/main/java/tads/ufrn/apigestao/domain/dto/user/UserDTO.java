package tads.ufrn.apigestao.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.Charging;

import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.Inspector;
import tads.ufrn.apigestao.domain.Seller;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorDTO;
import tads.ufrn.apigestao.domain.dto.seller.SellerDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String name;
    private String cpf;
    private String position;
}
