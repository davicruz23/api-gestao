package tads.ufrn.apigestao.domain.dto.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.dto.address.UpsertAddressDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertClientDTO {

    @NotBlank(message = "O nome do cliente é obrigatório")
    private String name;

    @NotBlank(message = "O CPF do cliente é obrigatório")
    private String cpf;

    @NotBlank(message = "O telefone do cliente é obrigatório")
    private String phone;

    @NotNull(message = "O endereço do cliente é obrigatório")
    @Valid
    private UpsertAddressDTO address;
    //private List<SaleDTO> sales;
}
