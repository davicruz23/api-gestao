package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Seller;
import tads.ufrn.apigestao.domain.dto.sale.SaleDTO;
import tads.ufrn.apigestao.domain.dto.seller.SellerDTO;

public class SellerMapper {
    public static SellerDTO mapper(Seller src){
        return SellerDTO.builder()
                .id(src.getId())
                //.user(src.getUser())
                //.preSales(src.getPreSales().stream().map(PreSaleMapper::mapper).toList())
                .build();
    }
}
