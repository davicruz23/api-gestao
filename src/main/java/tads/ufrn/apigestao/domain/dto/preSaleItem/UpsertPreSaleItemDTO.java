package tads.ufrn.apigestao.domain.dto.preSaleItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertPreSaleItemDTO {

    private Long id;

    @NotNull(message = "O produto é obrigatório")
    @Positive(message = "O produto informado é inválido")
    private Long productId;

    @NotNull(message = "A quantidade do produto é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    private Integer quantity;
}
