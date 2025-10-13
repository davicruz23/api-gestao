package tads.ufrn.apigestao.domain.dto.inspector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.Client;
import tads.ufrn.apigestao.domain.dto.client.ClientHistoryDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectorHistoryPreSaleDTO {

    private Long id;
    private String preSaleDate;
    private String status;
    private Double totalPreSale;
    private ClientHistoryDTO client;
}
