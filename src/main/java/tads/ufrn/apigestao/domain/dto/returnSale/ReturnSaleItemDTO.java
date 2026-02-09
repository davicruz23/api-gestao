package tads.ufrn.apigestao.domain.dto.returnSale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnSaleItemDTO {

    private Long productId;
    private Integer quantityReturned;

}
