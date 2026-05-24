package tads.ufrn.apigestao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tads.ufrn.apigestao.domain.CommissionHistory;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CommissionHistoryRepository extends JpaRepository<CommissionHistory, Long> {

    Page<CommissionHistory> findByCollectorId(Long collectorId, Pageable pageable);

    Page<CommissionHistory> findBySellerId(Long sellerId, Pageable pageable);

    Page<CommissionHistory> findByCollectorIsNotNull(Pageable pageable);

    Page<CommissionHistory> findBySellerIsNotNull(Pageable pageable);

    @Query("""
    select coalesce(sum(ch.amount), 0)
    from CommissionHistory ch
    where ch.seller.id = :sellerId
      and ch.startDate >= :startDate
      and ch.endDate <= :endDate
""")
    BigDecimal sumPaidAmountBySellerAndPeriod(
            @Param("sellerId") Long sellerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
    select coalesce(sum(ch.amount), 0)
    from CommissionHistory ch
    where ch.collector.id = :collectorId
      and ch.startDate >= :startDate
      and ch.endDate <= :endDate
""")
    BigDecimal sumPaidAmountByCollectorAndPeriod(
            @Param("collectorId") Long collectorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
