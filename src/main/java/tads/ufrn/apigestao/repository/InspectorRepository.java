package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.Inspector;

@Repository
public interface InspectorRepository extends JpaRepository<Inspector, Long> {
}
