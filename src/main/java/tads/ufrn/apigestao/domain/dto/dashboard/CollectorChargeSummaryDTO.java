package tads.ufrn.apigestao.domain.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectorChargeSummaryDTO {
    private String collectorName;
    private Long chargeCount;
    private BigDecimal totalAmount;
}
