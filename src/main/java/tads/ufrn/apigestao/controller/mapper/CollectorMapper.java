package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.dto.collector.CollectorDTO;

public class CollectorMapper {
    public static CollectorDTO mapper(Collector src){
        return CollectorDTO.builder()
                .id(src.getId())
                .user(src.getUser())
                .sales(src.getSales().stream().map(SaleMapper::mapper).toList())
                .build();
    }
}
