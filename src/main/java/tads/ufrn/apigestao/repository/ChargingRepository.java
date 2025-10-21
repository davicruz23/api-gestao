package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.Charging;

import java.util.List;

@Repository
public interface ChargingRepository extends JpaRepository<Charging, Long> {

    List<Charging> findAllByUserIdAndDeletedAtIsNull(Long userId);

    List<Charging> findAllByDeletedAtIsNull();

}
