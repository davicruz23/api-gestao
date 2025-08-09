package tads.ufrn.apigestao.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertProductDTO {

    private String name;
    private String brand;
    private Integer amount;
    private Double value;
    private Integer status;
}
