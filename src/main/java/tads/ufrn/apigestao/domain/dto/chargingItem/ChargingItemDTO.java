package tads.ufrn.apigestao.domain.dto.chargingItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChargingItemDTO {

    private Long id;
    private Long productId;
    private Long chargingId;
    private Integer quantity;
}
