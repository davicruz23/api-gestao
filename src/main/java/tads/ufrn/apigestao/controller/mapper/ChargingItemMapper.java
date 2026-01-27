package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.ChargingItem;
import tads.ufrn.apigestao.domain.dto.chargingItem.ChargingItemDTO;
import tads.ufrn.apigestao.enums.ProductStatus;

public class ChargingItemMapper {

    private static int resolveStatus(Integer amount) {
        if (amount == null) {
            return ProductStatus.INDISPONIVEL.getValue();
        }

        if (amount == 0) {
            return ProductStatus.ZERADO.getValue();
        }

        if (amount <= 30) {
            return ProductStatus.POUCO.getValue();
        }

        return ProductStatus.DISPONIVEL.getValue();
    }

    public static ChargingItemDTO mapper(ChargingItem src) {
        return ChargingItemDTO.builder()
                .id(src.getId())
                .productId(src.getProduct().getId())
                .chargingId(src.getCharging().getId())
                .quantity(src.getQuantity())
                .nameProduct(src.getProduct().getName())
                .brand(src.getProduct().getBrand())
                .priceProduct(src.getProduct().getValue())
                .status(resolveStatus(src.getQuantity()))
                .build();
    }
}
