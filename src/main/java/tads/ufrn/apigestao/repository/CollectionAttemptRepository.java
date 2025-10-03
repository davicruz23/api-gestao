package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tads.ufrn.apigestao.domain.CollectionAttempt;

import java.util.List;

public interface CollectionAttemptRepository extends JpaRepository<CollectionAttempt, Long> {

    List<CollectionAttempt> findByCollectorId(Long collectorId);
    List<CollectionAttempt> findByInstallmentId(Long installmentId);

    @Query("""
    SELECT ca
    FROM CollectionAttempt ca
    WHERE ca.collector.id = :collectorId
      AND ca.installment.sale.id = :saleId
      AND ca.type = 'PAYMENT'
""")
    List<CollectionAttempt> findPaidAttemptsByCollectorAndSale(
            @Param("collectorId") Long collectorId,
            @Param("saleId") Long saleId
    );
}

