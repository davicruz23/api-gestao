package tads.ufrn.apigestao.domain.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerCommissionDTO {

    private Long sellerId;
    private String sellerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double commission;
}
