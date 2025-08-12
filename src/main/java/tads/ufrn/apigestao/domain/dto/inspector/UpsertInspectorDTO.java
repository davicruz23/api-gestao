package tads.ufrn.apigestao.domain.dto.inspector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.dto.preSale.PreSaleDTO;
import tads.ufrn.apigestao.domain.dto.user.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertInspectorDTO {

    private Long id;
    private LocalDateTime inspectionData;
    private String statusId;
    private String observation;
    private UserDTO userName;
    private List<PreSaleDTO> preSales;
}

