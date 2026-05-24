package tads.ufrn.apigestao.domain.dto.commissionHistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.enums.CommissionReason;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerCommissionRequestDTO {

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal paymentPercentage;
    private CommissionReason reason;
}
