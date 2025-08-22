package tads.ufrn.apigestao.domain.dto.client;

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

    private Long id;
    private String name;
    private String cpf;
    private String phone;
    private UpsertAddressDTO address;
    //private List<SaleDTO> sales;
}
