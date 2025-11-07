package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.Charging;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChargingRepository extends JpaRepository<Charging, Long> {

    List<Charging> findAllByUserIdAndDeletedAtIsNull(Long userId);

    List<Charging> findAllByDeletedAtIsNull();

    @Query("""
    SELECT COUNT(*)
    FROM Charging c
""")
    Long countDistinctCities();

    @Query("""
        SELECT c FROM Charging c
        LEFT JOIN FETCH c.items i
        LEFT JOIN FETCH i.product p
        WHERE c.id = :id
    """)
    Optional<Charging> findWithItems(Long id);

}
