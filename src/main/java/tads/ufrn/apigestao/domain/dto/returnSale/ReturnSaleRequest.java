package tads.ufrn.apigestao.domain.dto.returnSale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnSaleRequest {

    private List<ReturnSaleItemDTO> items;
    private Integer status;
}