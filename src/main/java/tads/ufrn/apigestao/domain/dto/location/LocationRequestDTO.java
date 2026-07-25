package tads.ufrn.apigestao.domain.dto.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequestDTO {

    private Long userId;
    private Double latitude;
    private Double longitude;
}