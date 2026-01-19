package tads.ufrn.apigestao.domain.dto.charging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateChargingItemDTO {
    private Long productId;
    private int quantityToAdd;
}
