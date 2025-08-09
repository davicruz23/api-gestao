package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Client;
import tads.ufrn.apigestao.domain.dto.client.ClientDTO;

public class ClientMapper {
    public static ClientDTO mapper(Client src){
        return ClientDTO.builder()
                .name(src.getName())
                .cpf(src.getCpf())
                .phone(src.getName())
                .address(src.getAddress())
                //.sales(src.getSales().stream().map(SaleMapper::mapper).toList())
                .build();
    }
}
