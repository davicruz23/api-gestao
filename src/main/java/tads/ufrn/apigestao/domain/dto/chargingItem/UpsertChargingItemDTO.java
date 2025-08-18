package tads.ufrn.apigestao.domain.dto.chargingItem;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertChargingItemDTO {
    private Long productId;
    private Integer quantity;
}
