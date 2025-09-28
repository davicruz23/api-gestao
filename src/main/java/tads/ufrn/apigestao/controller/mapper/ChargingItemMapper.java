package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.ChargingItem;
import tads.ufrn.apigestao.domain.dto.chargingItem.ChargingItemDTO;

public class ChargingItemMapper {
    public static ChargingItemDTO mapper(ChargingItem src) {
        return ChargingItemDTO.builder()
                .id(src.getId())
                .productId(src.getProduct().getId())
                .chargingId(src.getCharging().getId())
                .quantity(src.getQuantity())
                .nameProduct(src.getProduct().getName())
                .brand(src.getProduct().getBrand())
                .priceProduct(src.getProduct().getValue())
                .build();
    }
}
