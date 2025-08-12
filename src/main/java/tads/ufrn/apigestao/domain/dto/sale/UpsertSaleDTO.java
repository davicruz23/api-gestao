package tads.ufrn.apigestao.domain.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.dto.client.ClientDTO;
import tads.ufrn.apigestao.domain.dto.collector.CollectorDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertSaleDTO {

    private Long id;
    private String numberSale;
    private LocalDateTime saleDate;
    private Integer paymentType;
    private Integer nParcel;
    private ClientDTO client;
    private List<ProductDTO> products;
    private CollectorDTO collector;
}
