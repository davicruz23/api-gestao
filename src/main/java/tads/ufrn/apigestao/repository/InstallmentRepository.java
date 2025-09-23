package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.Installment;

import java.util.List;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {

    List<Installment> findBySaleId(Long saleId);
}
