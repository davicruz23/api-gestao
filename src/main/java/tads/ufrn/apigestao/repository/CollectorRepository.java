package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tads.ufrn.apigestao.domain.Collector;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CollectorRepository extends JpaRepository<Collector, Long> {

    Optional<Collector> findByUserId(Long userId);

    @Query("SELECT c.user.name, COUNT(cl), SUM(cl.total) " +
            "FROM Collector c LEFT JOIN c.sales cl " +
            "WHERE (:startDate IS NULL OR cl.saleDate >= :startDate) " +
            "AND (:endDate IS NULL OR cl.saleDate <= :endDate) " +
            "GROUP BY c.id, c.user.name")
    List<Object[]> findCollectorsWithChargeCountAndTotal(@Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);


}
