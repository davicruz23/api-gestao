package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Address;
import tads.ufrn.apigestao.domain.dto.address.AddressDTO;
import tads.ufrn.apigestao.domain.dto.address.AddressHistoryDTO;

public class AddressMapper {
    public static AddressDTO mapper(Address src) {
        return AddressDTO.builder()
                .id(src.getId())
                .city(src.getCity())
                .street(src.getStreet())
                .number(src.getNumber())
                .zipCode(src.getZipCode())
                .complement(src.getComplement())
                .build();
    }

    public static AddressHistoryDTO addressClientHistory(Address src) {
        return AddressHistoryDTO.builder()
                .city(src.getCity())
                .street(src.getStreet())
                .number(src.getNumber())
                .build();
    }
}
