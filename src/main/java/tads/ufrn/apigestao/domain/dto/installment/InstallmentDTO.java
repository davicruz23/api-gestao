package tads.ufrn.apigestao.domain.dto.installment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.Sale;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstallmentDTO {

    private Long id;
    private String dueDate;
    private Double amount;
    private boolean paid = false;
    private InstallmentStatusDTO status;
    private Boolean isValid;
    private Double attemptLatitude;
    private Double attemptLongitude;
}
