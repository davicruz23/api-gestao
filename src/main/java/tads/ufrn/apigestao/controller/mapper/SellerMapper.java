package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Seller;
import tads.ufrn.apigestao.domain.dto.sale.SaleDTO;
import tads.ufrn.apigestao.domain.dto.seller.SellerDTO;
import tads.ufrn.apigestao.domain.dto.seller.SellerDetailsDTO;

public class SellerMapper {
    public static SellerDTO mapper(Seller src){
        return SellerDTO.builder()
                .id(src.getId())
                .user(src.getUser())
                .build();
    }

    public static SellerDetailsDTO mapperDetails(Seller src){
        return SellerDetailsDTO.builder()
                .idSeller(src.getId())
                .nomeSeller(src.getUser().getName())
                .build();
    }
}
