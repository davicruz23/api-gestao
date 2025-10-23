package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.PreSale;
import tads.ufrn.apigestao.domain.dto.preSale.PreSaleDTO;
import tads.ufrn.apigestao.domain.dto.seller.SellerDetailsDTO;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;

public class PreSaleMapper {
    public static PreSaleDTO mapper(PreSale src){
        return PreSaleDTO.builder()
                .id(src.getId())
                .preSaleDate(src.getPreSaleDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"))))
                .seller(SellerMapper.mapperDetails(src.getSeller()))
                .client(src.getClient())
                .inspector(src.getInspector().getUser().getName())
                .totalPreSale(src.getTotalPreSale())
                .status(src.getStatus().toString())
                .items(src.getItems().stream().map(PreSaleItemMapper::mapper).collect(Collectors.toList()))
                .build();
    }
}

