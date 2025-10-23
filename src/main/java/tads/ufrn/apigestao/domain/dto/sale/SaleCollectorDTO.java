package tads.ufrn.apigestao.domain.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.dto.client.ClientDTO;
import tads.ufrn.apigestao.domain.dto.client.ClientSaleDTO;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductSaleDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleCollectorDTO {

    private Long id;
    private String saleDate;
    private ClientSaleDTO client;
    private List<ProductSaleDTO> products;
    private List<InstallmentDTO> installments;
    private Double latitude;
    private Double longitude;
}