package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.PreSale;
import tads.ufrn.apigestao.domain.dto.preSale.PreSaleDTO;
import tads.ufrn.apigestao.domain.dto.seller.SellerDetailsDTO;

import java.util.stream.Collectors;

public class PreSaleMapper {
    public static PreSaleDTO mapper(PreSale src){
        return PreSaleDTO.builder()
                .id(src.getId())
                .preSaleDate(src.getPreSaleDate())
                .seller(SellerMapper.mapperDetails(src.getSeller()))
                .client(src.getClient())
                .inspector(src.getInspector().getUser().getName())
                .status(src.getStatus().toString())
                .items(src.getItems().stream().map(PreSaleItemMapper::mapper).collect(Collectors.toList()))
                .build();
    }
}

