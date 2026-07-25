package tads.ufrn.apigestao.domain.dto.preSale;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.dto.client.UpsertClientDTO;
import tads.ufrn.apigestao.domain.dto.preSaleItem.UpsertPreSaleItemDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertPreSaleDTO {

    private Long id;

    private Long clientId;

    private LocalDate preSaleDate;

    private Long sellerId;

    @NotNull(message = "Os dados do cliente são obrigatórios")
    @Valid
    private UpsertClientDTO client;

    @NotEmpty(message = "Selecione pelo menos um produto")
    @Valid
    private List<UpsertPreSaleItemDTO> products;

    private Long chargingId;

    private String uuidPreSale;
}