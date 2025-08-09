package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Product;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;

public class ProductMapper {
    public static ProductDTO mapper(Product src){
        return ProductDTO.builder()
                .id(src.getId())
                .name(src.getName())
                .brand(src.getBrand())
                .amount(src.getAmount())
                .value(src.getValue())
                .status(src.getStatus().toString())
                .build();
    }
}
