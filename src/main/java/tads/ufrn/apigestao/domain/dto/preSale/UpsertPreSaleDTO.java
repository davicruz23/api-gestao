package tads.ufrn.apigestao.domain.dto.preSale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.client.ClientDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertPreSaleDTO {

    private Long id;
    private LocalDateTime preSaleDate;
    private User userName;
    private ClientDTO clientName;
    private List<ProductDTO> products;
}
