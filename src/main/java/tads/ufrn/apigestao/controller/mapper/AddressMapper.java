package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Address;
import tads.ufrn.apigestao.domain.dto.address.AddressDTO;

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
}
