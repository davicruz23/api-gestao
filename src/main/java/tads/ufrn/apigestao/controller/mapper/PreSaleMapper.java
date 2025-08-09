package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.PreSale;
import tads.ufrn.apigestao.domain.dto.preSale.PreSaleDTO;

public class PreSaleMapper {
    public static PreSaleDTO mapper(PreSale src){
        return PreSaleDTO.builder()
                .id(src.getId())
                .preSaleDate(src.getPreSaleDate())
                //.userName(src.getSeller().getUser())
                //.clientName(src.getClient())
                //.products(src.getProducts().stream().map(ProductMapper::mapper).toList())
                .build();
    }
}

