package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.PreSale;
import tads.ufrn.apigestao.domain.dto.preSale.PreSaleDTO;

import java.util.stream.Collectors;

public class PreSaleMapper {
    public static PreSaleDTO mapper(PreSale src){
        return PreSaleDTO.builder()
                .id(src.getId())
                .preSaleDate(src.getPreSaleDate())
                .seller(src.getSeller().getUser())
                .client(src.getClient())
                .inspector(src.getInspector().getUser().getName())
                .status(src.getStatus().toString())
                .items(src.getItems().stream().map(PreSaleItemMapper::mapper).collect(Collectors.toList()))
                .build();
    }
}

