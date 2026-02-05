package tads.ufrn.apigestao.domain.dto.returnSale;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.enums.SaleStatus;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleReturnDTO {

    private Long id;
    private Long saleId;
    private Long productId;
    private OffsetDateTime returnDate;
    private SaleStatus saleStatus;
}
