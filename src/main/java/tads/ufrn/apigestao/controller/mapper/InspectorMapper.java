package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Inspector;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorDTO;

public class InspectorMapper {
    public static InspectorDTO mapper(Inspector src){
        return InspectorDTO.builder()
                .id(src.getId())
                .inspectionData(src.getInspectionData())
                .observation(src.getObservation())
                //.userName(src.getUser().getName())
                //.preSales(src.getPreSales().stream().map(PreSaleMapper::mapper).toList())
                .build();
    }
}
