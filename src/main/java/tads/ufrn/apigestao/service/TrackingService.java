package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.UserLocation;
import tads.ufrn.apigestao.domain.dto.location.CollectorRouteDTO;
import tads.ufrn.apigestao.domain.dto.location.CollectorTrackingDTO;
import tads.ufrn.apigestao.domain.dto.location.LocationPointDTO;
import tads.ufrn.apigestao.repository.CollectorRepository;
import tads.ufrn.apigestao.repository.UserLocationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final CollectorRepository collectorRepository;
    private final UserLocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<CollectorTrackingDTO> getCollectorsTracking() {
        return collectorRepository.findAll()
                .stream()
                .map(this::toTrackingDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CollectorRouteDTO getCollectorRoute(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    ) {
        Collector collector = collectorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cobrador não encontrado para o usuário informado."
                        )
                );

        List<UserLocation> locations;

        if (start != null && end != null) {
            locations =
                    locationRepository
                            .findAllByUserIdAndCapturedAtBetweenOrderByCapturedAtAsc(
                                    userId,
                                    start,
                                    end
                            );
        } else {
            locations =
                    locationRepository
                            .findAllByUserIdOrderByCapturedAtAsc(userId);
        }

        List<LocationPointDTO> points = locations
                .stream()
                .map(location -> new LocationPointDTO(
                        location.getId(),
                        location.getLatitude(),
                        location.getLongitude(),
                        location.getCapturedAt()
                ))
                .toList();

        return new CollectorRouteDTO(
                collector.getId(),
                userId,
                getCollectorName(collector),
                points
        );
    }

    private CollectorTrackingDTO toTrackingDTO(Collector collector) {
        Long userId = collector.getUser().getId();

        UserLocation latestLocation =
                locationRepository
                        .findFirstByUserIdOrderByCapturedAtDesc(userId)
                        .orElse(null);

        if (latestLocation == null) {
            return new CollectorTrackingDTO(
                    collector.getId(),
                    userId,
                    getCollectorName(collector),
                    null,
                    null,
                    null,
                    false
            );
        }

        boolean online = latestLocation
                .getCapturedAt()
                .isAfter(LocalDateTime.now().minusMinutes(5));

        return new CollectorTrackingDTO(
                collector.getId(),
                userId,
                getCollectorName(collector),
                latestLocation.getLatitude(),
                latestLocation.getLongitude(),
                latestLocation.getCapturedAt(),
                online
        );
    }

    private String getCollectorName(Collector collector) {
        return collector.getUser().getName();
    }
}