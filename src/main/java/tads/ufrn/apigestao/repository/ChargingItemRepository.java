package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.ChargingItem;

import java.util.List;

@Repository
public interface ChargingItemRepository extends JpaRepository<ChargingItem, Long> {

    List<ChargingItem> findByChargingId(Long chargingId);

}
