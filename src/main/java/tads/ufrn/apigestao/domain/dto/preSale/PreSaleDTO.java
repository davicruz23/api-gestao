package tads.ufrn.apigestao.domain.dto.preSale;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.Client;
import tads.ufrn.apigestao.domain.Inspector;
import tads.ufrn.apigestao.domain.Product;
import tads.ufrn.apigestao.domain.Users;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreSaleDTO {

    private Long id;
    private LocalDateTime preSaleDate;
    private Users userName;
    private String clientName;
    private String inspector;
    private List<ProductDTO> products;
}
