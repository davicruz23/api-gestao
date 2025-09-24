package tads.ufrn.apigestao.domain.dto.collector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectorCommissionDTO {
    private Long collectorId;
    private String collectorName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double commission;
}
