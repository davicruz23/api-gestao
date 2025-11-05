package tads.ufrn.apigestao.domain.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSaleDTO {

    private Long total;
    private BigDecimal totalSale;
    private Double percentual;
}
