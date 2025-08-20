package tads.ufrn.apigestao.domain.dto.preSaleItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreSaleItemDTO {

    private Long productId;
    private String productName;
    private Integer quantity;
}
