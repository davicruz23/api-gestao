package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Product;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductSaleDTO;
import tads.ufrn.apigestao.domain.dto.product.UptadeProductDTO;
import tads.ufrn.apigestao.enums.ProductStatus;

public class ProductMapper {

    private static int resolveStatus(Integer amount) {
        if (amount == null) {
            return ProductStatus.INDISPONIVEL.getValue();
        }

        if (amount == 0) {
            return ProductStatus.ZERADO.getValue();
        }

        if (amount <= 10) {
            return ProductStatus.MUITOPOUCO.getValue();
        }

        if (amount <= 30) {
            return ProductStatus.POUCO.getValue();
        }

        return ProductStatus.DISPONIVEL.getValue();
    }

    public static ProductDTO mapper(Product src){
        return ProductDTO.builder()
                .id(src.getId())
                .name(src.getName())
                .brand(src.getBrand())
                .amount(src.getAmount())
                .value(src.getValue())
                .status(resolveStatus(src.getAmount()))
                .build();
    }

    public static ProductSaleDTO mapperProductSale(Product product, Integer quantity){
        return ProductSaleDTO.builder()
                .id(product.getId())
                .nameProduct(product.getName())
                .quantity(quantity)
                .build();
    }

    public static UptadeProductDTO mapperUptadeProduct(Product src){
        return UptadeProductDTO.builder()
                .value(src.getValue())
                .amount(src.getAmount())
                .build();
    }
}
