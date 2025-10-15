package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.ApprovalLocation;
import tads.ufrn.apigestao.domain.CollectionAttempt;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalLocationRepository extends JpaRepository<ApprovalLocation, Long> {

    @Query("""
    SELECT ap
    FROM ApprovalLocation ap
    WHERE ap.sale.id = :saleId
""")
    List<ApprovalLocation> locationBySaleId(@Param("saleId") Long saleId);

    Optional<ApprovalLocation> findBySaleId(@Param("saleId") Long saleId);
}
