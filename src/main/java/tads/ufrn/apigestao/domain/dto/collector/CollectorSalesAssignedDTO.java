package tads.ufrn.apigestao.domain.dto.collector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectorSalesAssignedDTO {

    private Long collectorId;
    private String city;
    private int totalSalesAssigned;
}
