package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
}
