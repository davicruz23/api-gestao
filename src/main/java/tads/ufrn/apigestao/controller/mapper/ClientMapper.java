package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Client;
import tads.ufrn.apigestao.domain.dto.client.ClientDTO;

public class ClientMapper {
    public static ClientDTO mapper(Client src){
        return ClientDTO.builder()
                .id(src.getId())
                .name(src.getName())
                .cpf(src.getCpf())
                .phone(src.getName())
                .address(String.valueOf(src.getAddress()))
                //.sales(src.getstream().map(SaleMapper::mapper).toList())
                .build();
    }
}
