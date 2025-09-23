package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.collector.CollectorSalesAssignedDTO;
import tads.ufrn.apigestao.service.CollectorService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/collector")
public class CollectorController {

    private final CollectorService service;

    @PostMapping("/{collectorId}/assign/{city}")
    public CollectorSalesAssignedDTO assignSalesByCity(
            @PathVariable Long collectorId,
            @PathVariable String city
    ) {
        return service.assignSalesByCity(collectorId, city);
    }

    @GetMapping("/{collectorId}/sales")
    public List<Sale> getSales(@PathVariable Long collectorId) {
        return service.getSales(collectorId);
    }
}
