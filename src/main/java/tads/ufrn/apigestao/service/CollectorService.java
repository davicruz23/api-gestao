package tads.ufrn.apigestao.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.collector.CollectorSalesAssignedDTO;
import tads.ufrn.apigestao.repository.CollectorRepository;
import tads.ufrn.apigestao.repository.SaleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectorService {

    private final CollectorRepository repository;
    private final SaleRepository saleRepository;
    private final SaleService saleService;

    @Transactional
    public CollectorSalesAssignedDTO assignSalesByCity(Long collectorId, String city) {
        Collector collector = repository.findById(collectorId)
                .orElseThrow(() -> new RuntimeException("Collector não encontrado"));
        List<Sale> sales = saleRepository.findUnassignedSalesByCity(city);
        for (Sale sale : sales) {
            sale.setCollector(collector);
        }
        saleRepository.saveAll(sales);

        return new CollectorSalesAssignedDTO(
                collector.getId(),
                city,
                sales.size()
        );
    }

    public List<Sale> getSales(Long collectorId) {
        return saleService.getSalesByCollector(collectorId);
    }
}
