package tads.ufrn.apigestao.domain.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.Client;
import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDTO {

    private Long id;
    private String numberSale;
    private LocalDateTime saleDate;
    private Integer paymentType;
    private Integer nParcel;
    private Client client;
    private List<ProductDTO> products;
    private Collector collector;
}
