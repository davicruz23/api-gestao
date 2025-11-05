package tads.ufrn.apigestao.domain.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardProductSalesDTO {
    private Long productId;
    private String productName;
    private Long totalQuantity;
    private Double totalValue;
}
