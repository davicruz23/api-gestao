package tads.ufrn.apigestao.domain.dto.returnSale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.enums.SaleStatus;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleReturnInfoDTO {

    private Long saleReturnId;
    private Long productId;
    private String productName;
    private Integer quantityReturned;
    private BigDecimal valueAbatido;
    private String returnDate;
    private String status;
}
