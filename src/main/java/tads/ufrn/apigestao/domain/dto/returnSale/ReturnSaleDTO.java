package tads.ufrn.apigestao.domain.dto.returnSale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.enums.ReturnReason;
import tads.ufrn.apigestao.enums.SaleStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnSaleDTO {

    private Long saleId;
    private int status;

}
