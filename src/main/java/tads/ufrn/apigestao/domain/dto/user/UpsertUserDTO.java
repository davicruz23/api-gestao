package tads.ufrn.apigestao.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertUserDTO {

    private String name;
    private String cpf;
    private Integer position;
    private String password;
}
