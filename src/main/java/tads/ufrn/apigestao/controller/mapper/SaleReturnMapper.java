package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.SaleReturn;
import tads.ufrn.apigestao.domain.dto.returnSale.SaleReturnData;

import java.util.Map;

public class SaleReturnMapper {
    public static SaleReturnData mapper(SaleReturn src, Map<Long, String> productNameMap) {
        return SaleReturnData.builder()
                .saleReturnId(src.getId())
                .returnDate(src.getReturnDate())
                .saleStatus(src.getSaleStatus().getValue())
                .productIdReturned(src.getProductId())
                .productNameReturned(productNameMap.get(src.getProductId()))
                .quantityReturned(src.getQuantityReturned())
                .description(src.getDescription())
                .saleDTO(SaleMapper.toReturnDTO(src.getSale()))
                .build();
    }
}
