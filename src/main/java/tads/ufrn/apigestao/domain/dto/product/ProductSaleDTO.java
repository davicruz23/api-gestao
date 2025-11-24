package tads.ufrn.apigestao.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSaleDTO {

    private Long id;
    private String nameProduct;
    private Integer quantity;
}
