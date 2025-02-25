package tads.ufrn.apigestao.domain.dto.collector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.Users;
import tads.ufrn.apigestao.domain.dto.sale.SaleDTO;
import tads.ufrn.apigestao.domain.dto.user.UserDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectorDTO {

    private Long id;
    private Users user;
    private List<SaleDTO> sales;
}
