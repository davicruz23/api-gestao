package tads.ufrn.apigestao.domain.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.dto.address.AddressDTO;
import tads.ufrn.apigestao.domain.dto.address.AddressHistoryDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientHistoryDTO {

//    private Long id;
    private String name;
    private String cpf;
    private AddressHistoryDTO address;
}
