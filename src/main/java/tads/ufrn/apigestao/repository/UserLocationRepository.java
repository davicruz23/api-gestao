package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.UserLocation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

    Optional<UserLocation> findByUserId(Long userId);

    Optional<UserLocation> findFirstByUserIdOrderByCapturedAtDesc(
            Long userId
    );

    List<UserLocation> findAllByUserIdOrderByCapturedAtAsc(
            Long userId
    );

    List<UserLocation> findAllByUserIdAndCapturedAtBetweenOrderByCapturedAtAsc(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );
}