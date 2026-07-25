package tads.ufrn.apigestao.domain.dto.location;

import java.util.List;

public record CollectorRouteDTO(
        Long collectorId,
        Long userId,
        String collectorName,
        List<LocationPointDTO> points
) {
}