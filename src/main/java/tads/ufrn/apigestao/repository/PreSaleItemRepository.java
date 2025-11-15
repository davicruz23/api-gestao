package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.PreSaleItem;
import tads.ufrn.apigestao.domain.dto.dashboard.DashboardProductSalesDTO;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PreSaleItemRepository extends JpaRepository<PreSaleItem, Long> {

    @Query("""
    SELECT new tads.ufrn.apigestao.domain.dto.dashboard.DashboardProductSalesDTO(
        p.id,
        p.name,
        SUM(i.quantity),
        SUM(i.quantity * p.value)
    )
    FROM PreSaleItem i
    JOIN i.product p
    JOIN i.preSale ps
    WHERE (:startDate IS NULL OR ps.preSaleDate >= :startDate)
      AND (:endDate IS NULL OR ps.preSaleDate <= :endDate)
    GROUP BY p.id, p.name
    ORDER BY SUM(i.quantity) DESC
    LIMIT 5
""")
    List<DashboardProductSalesDTO> findTotalProductsSoldByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
