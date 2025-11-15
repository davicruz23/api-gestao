package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.Installment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {

    List<Installment> findBySaleId(Long saleId);

    @Query("""
        SELECT COALESCE(SUM(i.amount), 0)
        FROM Installment i
        WHERE i.paid = TRUE
          AND i.paymentDate BETWEEN :inicio AND :fim
    """)
    BigDecimal findTotalCobradoPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

    @Query(value = """
        SELECT 
            s.collector_id,
            u.name AS collectorName,
            SUM(CASE 
                    WHEN i.paid = TRUE 
                     AND DATE(i.payment_date) = CURDATE() 
                        THEN i.amount 
                    ELSE 0 
                END) AS totalCollectedToday,
            SUM(CASE 
                    WHEN MONTH(i.due_date) = MONTH(CURDATE()) 
                     AND YEAR(i.due_date) = YEAR(CURDATE())
                        THEN i.amount
                    ELSE 0 
                END) AS totalToCollectThisMonth
        FROM installment i
        JOIN sale s       ON s.id = i.sale_id
        JOIN collector c  ON c.id = s.collector_id
        JOIN users u       ON u.id = c.user_id   -- 👈 AQUI PEGA O NOME CERTO
        GROUP BY s.collector_id, u.name
        ORDER BY totalCollectedToday DESC
        LIMIT 3
        """,
            nativeQuery = true)
    List<Object[]> findTopCollectorsStatus();


}