package tads.ufrn.apigestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tads.ufrn.apigestao.domain.Client;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT COUNT(c) FROM Client c")
    Long countTotalClients();

    List<Client> findByAddress_CityIgnoreCase(String city);

    @Query(value = """
        select 
            c.id,
            c.name,
            c.phone,
            a.city
        FROM Client c
        JOIN Address a ON a.id = c.id
        ORDER BY c.id DESC 
        LIMIT 6
    """,  nativeQuery = true)
    List<Object[]> findRecentClientsRaw();
}
