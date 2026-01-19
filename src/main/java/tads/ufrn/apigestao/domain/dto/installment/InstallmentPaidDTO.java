package tads.ufrn.apigestao.domain.dto.installment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentPaidDTO {

    private Long id;
    private LocalDate dueDate;
    private BigDecimal amount;
    private Boolean paid;
    private LocalDateTime paymentDate;
}
