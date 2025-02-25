package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.PreSale;

@Repository
public interface PreSaleRepository extends JpaRepository<PreSale, Long> {
}
