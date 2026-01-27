package tads.ufrn.apigestao.domain.dto.chargingItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChargingItemDTO {

    private Long id;
    private Long productId;
    private Long chargingId;
    private Integer quantity;
    private String nameProduct;
    private String brand;
    private BigDecimal priceProduct;
    private int status;
}
