package tads.ufrn.apigestao.domain.dto.sale;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.enums.PaymentType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovePreSaleDTO {
    private Long inspectorId;
    private PaymentType paymentMethod;
    private int installments;
}

