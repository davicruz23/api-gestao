package tads.ufrn.apigestao.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSaleDTO {

    private Long id;
    private String nameProduct;
    private Integer quantity;
    private BigDecimal price;
}
