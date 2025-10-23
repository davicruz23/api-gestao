package tads.ufrn.apigestao.domain.dto.preSale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.Client;

import tads.ufrn.apigestao.domain.PreSaleItem;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.preSaleItem.PreSaleItemDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.domain.dto.seller.SellerDetailsDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreSaleDTO {

    private Long id;
    private String preSaleDate;
    private SellerDetailsDTO seller;
    private Client client;
    private String inspector;
    private Double totalPreSale;
    private List<PreSaleItemDTO> items;
    private String status;
}
