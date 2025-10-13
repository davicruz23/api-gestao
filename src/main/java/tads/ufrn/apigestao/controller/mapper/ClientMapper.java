package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Client;
import tads.ufrn.apigestao.domain.dto.client.ClientDTO;
import tads.ufrn.apigestao.domain.dto.client.ClientHistoryDTO;
import tads.ufrn.apigestao.domain.dto.client.ClientSaleDTO;

public class ClientMapper {
        public static ClientDTO mapper(Client src){
            return ClientDTO.builder()
                    .id(src.getId())
                    .name(src.getName())
                    .cpf(src.getCpf())
                    .phone(src.getPhone())
                    .address(String.valueOf(src.getAddress()))
                    //.sales(src.getstream().map(SaleMapper::mapper).toList())
                    .build();
        }

    public static ClientSaleDTO clientSale(Client src) {
        return ClientSaleDTO.builder()
                .name(src.getName())
                .cpf(src.getCpf())
                .phone(src.getPhone())
                .address(
                        src.getAddress() != null
                                ? AddressMapper.mapper(src.getAddress())
                                : null
                )
                .build();
    }

    public static ClientHistoryDTO clientHistory(Client src) {
            return ClientHistoryDTO.builder()
                    .name(src.getName())
                    .cpf(src.getCpf())
                    .address(src.getAddress() != null ? AddressMapper.addressClientHistory(src.getAddress()) : null)
                    .build();
    }
}
