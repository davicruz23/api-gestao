package tads.ufrn.apigestao.domain.dto.charging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.controller.mapper.ChargingItemMapper;
import tads.ufrn.apigestao.domain.dto.chargingItem.ChargingItemDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChargingDTO {

    private Long id;
    private LocalDate chargingDate;
    private String userName;
    private String description;
    private LocalDate data;
    private List<ChargingItemDTO> chargingItems;
}
