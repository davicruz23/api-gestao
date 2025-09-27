package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tads.ufrn.apigestao.domain.CommissionHistory;

public interface CommissionHistoryRepository extends JpaRepository<CommissionHistory, Long> {
}
