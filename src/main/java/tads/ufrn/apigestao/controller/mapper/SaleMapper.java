package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.sale.SaleDTO;

import java.util.stream.Collectors;

public class SaleMapper {
    public static SaleDTO mapper(Sale src){
        return SaleDTO.builder()
                .id(src.getId())
                .numberSale(src.getNumberSale())
                //.paymentType(src.getPaymentType())
                //.nParcel(src.getNParcel())
                //.client(src.getClient())
                //.products(src.getProducts().stream().map(ProductMapper::mapper).toList())
                //.collector(src.getCollector())
                .build();
    }
}
