package tads.ufrn.apigestao.domain.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductSaleDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDTO {

    private Long id;
    private String numberSale;
    private String saleDate;
    private String paymentType;
    private Integer nParcel;
    private String clientName;
    private Double total;
    private Double longitude;
    private Double latitude;
    private List<ProductSaleDTO> products;
    private List<InstallmentDTO> installments;
}
