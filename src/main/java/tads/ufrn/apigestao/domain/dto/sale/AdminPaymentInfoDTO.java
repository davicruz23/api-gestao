package tads.ufrn.apigestao.domain.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPaymentInfoDTO {

    private Long saleId;
    private String clientName;
    private BigDecimal openBalance;
    private List<AdminPaymentInstallmentDTO> installments;

}