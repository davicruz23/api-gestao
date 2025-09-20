package tads.ufrn.apigestao.domain.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerDetailsDTO {

    private Long idSeller;
    private String nomeSeller;
}
