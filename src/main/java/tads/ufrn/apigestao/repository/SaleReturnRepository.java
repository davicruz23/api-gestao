package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.SaleReturn;

@Repository
public interface SaleReturnRepository extends JpaRepository<SaleReturn, Long> {

    boolean existsBySaleId(long saleId);
}
