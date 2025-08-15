package tads.ufrn.apigestao.domain.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.user.UserDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertSellerDTO {

    private Long id;
    private UserDTO user;
}
