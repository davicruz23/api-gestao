package tads.ufrn.apigestao.domain.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressHistoryDTO {

    private String city;
    private String street;
    private String number;
}
