package tads.ufrn.apigestao.domain.dto.collector;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.YearMonth;

@Data
@AllArgsConstructor
public class CollectorCommissionDTO {
    private Long collectorId;
    private String collectorName;
    private YearMonth month;
    private Double commission;
}
