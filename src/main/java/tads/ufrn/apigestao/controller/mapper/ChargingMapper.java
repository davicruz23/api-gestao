package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Charging;
import tads.ufrn.apigestao.domain.dto.charging.ChargingDTO;
import tads.ufrn.apigestao.domain.dto.charging.UpsertChargingDTO;

public class ChargingMapper {
    public static ChargingDTO chargingDTO(Charging src){
        return ChargingDTO.builder()
                .id(src.getId())
                .chargingDate(src.getDate())
                //.userName(src.getUser().getName())
                //.products(src.getProducts().stream().map(ProductMapper::mapper).toList())
                .build();

    }

    public static UpsertChargingDTO updateChargingDTO(Charging src){
        return UpsertChargingDTO.builder()
                //.products(src.getProducts().stream().map(ProductMapper::mapper).toList())
                .build();
    }
}
