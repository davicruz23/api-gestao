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
}
