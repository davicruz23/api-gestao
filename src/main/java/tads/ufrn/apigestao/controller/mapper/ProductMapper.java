package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Product;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductSaleDTO;
import tads.ufrn.apigestao.domain.dto.product.UptadeProductDTO;

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
