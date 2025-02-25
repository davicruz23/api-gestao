package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Users;
import tads.ufrn.apigestao.domain.dto.user.UserDTO;

public class UserMapper {
    public static UserDTO mapper(Users src){
        return UserDTO.builder()
                .id(src.getId())
                .name(src.getName())
                .function(src.getFunction())
                .password(src.getPassword())
                .seller(src.getSeller())
                .inspector(src.getInspector())
                .collector(src.getCollector())
                .build();
    }
}
