package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tads.ufrn.apigestao.domain.Collector;

import java.util.Optional;

public interface CollectorRepository extends JpaRepository<Collector, Long> {

    Optional<Collector> findByUserId(Long userId);


}
