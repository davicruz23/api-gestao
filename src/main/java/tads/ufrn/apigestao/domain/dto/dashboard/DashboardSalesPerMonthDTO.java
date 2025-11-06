package tads.ufrn.apigestao.domain.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSalesPerMonthDTO {

    private String mes;
    private Long totalVendas;
}
