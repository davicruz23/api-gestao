package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.PreSale;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.enums.PreSaleStatus;

import java.util.List;

@Repository
public interface PreSaleRepository extends JpaRepository<PreSale, Long> {

    List<PreSale> findByInspectorIdAndStatus(Long inspectorId, PreSaleStatus status);

    List<PreSale> findBySellerId(Long sellerId);

    @Query("""
    SELECT ps
    FROM PreSale ps
    WHERE ps.inspector.id = :inspectorId
      AND ps.status IN (
          tads.ufrn.apigestao.enums.PreSaleStatus.APROVADA,
          tads.ufrn.apigestao.enums.PreSaleStatus.RECUSADA
      )
""")
    List<PreSale> findAllByInspectorId(@Param("inspectorId") Long inspectorId);

    Long countByStatus(PreSaleStatus status);

}
