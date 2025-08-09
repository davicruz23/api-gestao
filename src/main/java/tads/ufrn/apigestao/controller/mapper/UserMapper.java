package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.user.UserDTO;

public class UserMapper {
    public static UserDTO mapper(User src){
        return UserDTO.builder()
                .id(src.getId())
                .name(src.getName())
                .cpf(src.getCpf())
                .position(src.getPosition().toString())
                //.seller(src.getSeller())
                //.inspector(src.getInspector())
                //.collector(src.getCollector())
                .build();
    }
}
