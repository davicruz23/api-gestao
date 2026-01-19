package tads.ufrn.apigestao.domain.dto.collector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectorCommissionDTO {
    private Long collectorId;
    private String collectorName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal commission;
}
