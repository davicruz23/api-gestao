package tads.ufrn.apigestao.domain.dto.collector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectorSalesDTO {
    private Long saleId;
    private LocalDateTime saleDate; // ADICIONADO
    private Double total;
    private Integer installmentsCount;
    private Boolean fullyPaid;
    private List<InstallmentDTO> installments;
}


