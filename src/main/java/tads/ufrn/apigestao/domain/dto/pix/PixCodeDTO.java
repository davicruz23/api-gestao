package tads.ufrn.apigestao.domain.dto.pix;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PixCodeDTO {
    private String pixString;
    private Double amount;
}
