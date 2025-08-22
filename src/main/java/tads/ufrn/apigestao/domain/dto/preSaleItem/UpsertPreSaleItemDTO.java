package tads.ufrn.apigestao.domain.dto.preSaleItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertPreSaleItemDTO {

    private Long id;
    private Long productId;
    private Integer quantity;
}
