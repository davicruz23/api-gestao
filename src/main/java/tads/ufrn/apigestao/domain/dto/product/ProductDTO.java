package tads.ufrn.apigestao.domain.dto.product;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.Charging;
import tads.ufrn.apigestao.domain.Client;
import tads.ufrn.apigestao.domain.Sale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    private String name;
    private String brand;
    private Integer amount;
    private Long statusId;
    private Double value;


}
