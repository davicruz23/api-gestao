package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.Installment;
import tads.ufrn.apigestao.domain.Sale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {

    List<Installment> findBySaleId(Long saleId);

    @Query("""
        SELECT COALESCE(SUM(i.paidAmount), 0)
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

    Optional<Installment> findFirstBySaleAndDueDateAfterOrderByDueDateAsc(
            Sale sale,
            LocalDate dueDate
    );

    @Modifying
    @Query("""
    UPDATE Installment i
       SET i.deletedAt = :deletedAt
     WHERE i.sale.id = :saleId
       AND i.deletedAt IS NULL
""")
    void softDeleteAllBySaleId(@Param("saleId") Long saleId,
                              @Param("deletedAt") OffsetDateTime deletedAt);


        @Modifying
        @Query("""
        update Installment i
           set i.deletedAt = :deletedAt
         where i.sale.id = :saleId
           and i.paid = false
           and i.deletedAt is null
    """)
        void softDeleteAllFutureBySaleId(@Param("saleId") Long saleId,
                                         @Param("deletedAt") OffsetDateTime deletedAt);

        List<Installment> findAllBySaleIdAndPaidFalseOrderByDueDateAsc(Long saleId);

    @Modifying
    @Query("""
    update Installment i
       set i.deletedAt = :deletedAt
     where i.sale.id = :saleId
       and i.paid = false
       and i.deletedAt is null
""")
    void softDeleteAllBySaleIdAndPaidFalse(Long saleId, OffsetDateTime deletedAt);


}