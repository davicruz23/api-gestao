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
public class CollectorCommissionDTOO {

    private Long collectorId;

    private String collectorName;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal totalCommission;

    private BigDecimal paymentPercentage;

    private BigDecimal previousPaidAmount;

    private BigDecimal amountToPay;

    private CommissionReason reason;
}
