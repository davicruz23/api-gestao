package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.ApprovalLocation;
import tads.ufrn.apigestao.domain.dto.LocationSaleDTO;

public class LocationSaleMapper {
    public static LocationSaleDTO mapper (ApprovalLocation src) {
        if (src == null) return null;

        Double latitude = src.getLatitude();
        Double longitude = src.getLongitude();

        LocationSaleDTO dto = LocationSaleDTO.builder()
                .sale(SaleMapper.mapperSaleLocation(src.getSale()))
                .build();

        dto.setMapsUrl(dto.generateMapsUrl(latitude, longitude));

        return dto;
    }
}
