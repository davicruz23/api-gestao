package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tads.ufrn.apigestao.domain.dto.location.LocationRequestDTO;
import tads.ufrn.apigestao.domain.UserLocation;
import tads.ufrn.apigestao.repository.UserLocationRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final UserLocationRepository repository;

    @Transactional
    public void saveLocation(LocationRequestDTO dto) {

        UserLocation location = new UserLocation();

        location.setUserId(dto.getUserId());
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setCapturedAt(LocalDateTime.now());

        repository.save(location);
    }

    @Transactional(readOnly = true)
    public UserLocation getLatestLocation(Long userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Localização não encontrada para o usuário"
                        )
                );
    }
}