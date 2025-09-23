package tads.ufrn.apigestao.domain.dto.installment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentPaidDTO {

    private Long id;
    private LocalDateTime dueDate;
    private Double amount;
    private Boolean paid;
    private LocalDateTime paymentDate;
}
