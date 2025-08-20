package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductSaleDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleDTO;

import java.util.stream.Collectors;

public class SaleMapper {
    public static SaleDTO mapper(Sale src){
        return SaleDTO.builder()
                .id(src.getId())
                .numberSale(src.getNumberSale())
                .saleDate(src.getSaleDate())
                .clientName(src.getPreSale().getClient().getName())
                .paymentType(src.getPaymentMethod().toString())
                .nParcel(src.getInstallments())
                .total(src.getTotal())
                .products(src.getPreSale().getItems().stream()
                        .map(item -> ProductMapper.mapperProductSale(item.getProduct()))
                        .toList()).build();
    }
}
