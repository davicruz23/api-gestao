package tads.ufrn.apigestao.domain.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.preSale.PreSaleDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerDTO {

    private Long id;
    private User user;
    private List<PreSaleDTO> preSales;

}
