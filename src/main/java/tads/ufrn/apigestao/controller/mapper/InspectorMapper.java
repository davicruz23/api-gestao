package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Inspector;
import tads.ufrn.apigestao.domain.PreSale;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorDTO;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorHistoryPreSaleDTO;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InspectorMapper {
    public static InspectorDTO mapper(Inspector src){
        return InspectorDTO.builder()
                .id(src.getId())
                .userName(src.getUser().getName())
                .preSales(src.getPreSales().stream().map(PreSaleMapper::mapper).toList())
                .build();
    }

    public static InspectorHistoryPreSaleDTO mapperHistory(PreSale src) {
        if (src == null) return null;

        return InspectorHistoryPreSaleDTO.builder()
                .id(src.getId())
                .preSaleDate(src.getPreSaleDate()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"))))
                .status(src.getStatus().toString())
                .totalPreSale(src.getTotalPreSale())
                .client(src.getClient() != null ? ClientMapper.clientHistory(src.getClient()) : null)
                .build();
    }
}
