package tads.ufrn.apigestao.domain.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPaymentInstallmentDTO {

    private Long id;
    private LocalDate dueDate;
    private BigDecimal amount;
    private boolean paid;

}