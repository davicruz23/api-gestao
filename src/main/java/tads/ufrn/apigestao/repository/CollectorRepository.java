package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tads.ufrn.apigestao.domain.Collector;

public interface CollectorRepository extends JpaRepository<Collector, Long> {


}
