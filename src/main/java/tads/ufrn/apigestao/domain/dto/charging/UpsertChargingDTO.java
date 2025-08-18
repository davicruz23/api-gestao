package tads.ufrn.apigestao.domain.dto.charging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.chargingItem.UpsertChargingItemDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertChargingDTO {

    private String description;
    private LocalDate date;
    private List<UpsertChargingItemDTO> items;
}
