package tads.ufrn.apigestao.domain.dto.returnSale;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.dto.sale.SaleDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleReturnDTO;
import tads.ufrn.apigestao.enums.SaleStatus;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleReturnData {
    private Long saleReturnId;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private OffsetDateTime  returnDate;
    private Integer saleStatus;
    private Long productIdReturned;
    private String productNameReturned;
    private Integer quantityReturned;
    private String description;
    private SaleReturnDTO saleDTO;
}
