package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Charging;
import tads.ufrn.apigestao.domain.dto.charging.ChargingDTO;
import tads.ufrn.apigestao.domain.dto.charging.UpsertChargingDTO;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;

public class ChargingMapper {
    public static ChargingDTO mapper(Charging src){
        return ChargingDTO.builder()
                .id(src.getId())
                .userName(src.getUser().getName())
                .chargingDate(src.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"))))
                .description(src.getDescription())
                .data(src.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"))))
                .chargingItems(src.getItems().stream().map(ChargingItemMapper::mapper).collect(Collectors.toList()))
                .build();
    }

    public static UpsertChargingDTO updateChargingDTO(Charging src){
        return UpsertChargingDTO.builder()
                //.products(src.getProducts().stream().map(ProductMapper::mapper).toList())
                .build();
    }
}
