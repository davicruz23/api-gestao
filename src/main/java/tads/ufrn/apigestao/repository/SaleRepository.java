package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.sale.SalesByCityDTO;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("""
    SELECT new tads.ufrn.apigestao.domain.dto.sale.SalesByCityDTO(c.address.city, COUNT(s))
    FROM Sale s
    JOIN s.preSale ps
    JOIN ps.client c
    GROUP BY c.address.city
""")
    List<SalesByCityDTO> countSaleByCity();

    @Query("""
    SELECT s
    FROM Sale s
    JOIN s.preSale ps
    JOIN ps.client c
    WHERE c.address.city = :city
    AND s.collector IS NULL
""")
    List<Sale> findUnassignedSalesByCity(String city);

    List<Sale> findByCollectorId(Long collectorId);

    @Query("""
    SELECT s
    FROM Sale s
    WHERE s.collector.id = :collectorId
      AND EXISTS (
          SELECT 1
          FROM Installment i
          WHERE i.sale.id = s.id
            AND i.paid = false
      )
""")
    List<Sale> findByCollectorIdWithPendingInstallments(@Param("collectorId") Long collectorId);

}
