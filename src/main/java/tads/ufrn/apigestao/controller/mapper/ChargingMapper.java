package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Charging;
import tads.ufrn.apigestao.domain.dto.charging.ChargingDTO;
import tads.ufrn.apigestao.domain.dto.charging.UpsertChargingDTO;

import java.util.stream.Collectors;

public class ChargingMapper {
    public static ChargingDTO mapper(Charging src){
        return ChargingDTO.builder()
                .id(src.getId())
                .userName(src.getUser().getName())
                .chargingDate(src.getCreatedAt())
                .description(src.getDescription())
                .chargingItems(src.getItems().stream().map(ChargingItemMapper::mapper).collect(Collectors.toList()))
                .build();
    }

    public static UpsertChargingDTO updateChargingDTO(Charging src){
        return UpsertChargingDTO.builder()
                //.products(src.getProducts().stream().map(ProductMapper::mapper).toList())
                .build();
    }
}
