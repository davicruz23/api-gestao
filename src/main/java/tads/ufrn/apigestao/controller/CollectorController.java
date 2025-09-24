package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.Installment;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.collector.CollectorCommissionDTO;
import tads.ufrn.apigestao.domain.dto.collector.CollectorSalesAssignedDTO;
import tads.ufrn.apigestao.domain.dto.collector.CollectorSalesDTO;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentPaidDTO;
import tads.ufrn.apigestao.service.CollectorService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/{collectorId}/salesssssss")
    public List<Sale> getSales(@PathVariable Long collectorId) {
        return service.getSales(collectorId);
    }

    @GetMapping("/{collectorId}/sales")
    public ResponseEntity<List<CollectorSalesDTO>> getCollectorSales(@PathVariable Long collectorId) {
        List<CollectorSalesDTO> salesDTO = service.getSalesByCollectorDTO(collectorId);
        return ResponseEntity.ok(salesDTO);
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payInstallment(@PathVariable Long id) {
        try {
            InstallmentPaidDTO paidInstallment = service.markAsPaid(id);
            return ResponseEntity.ok(paidInstallment);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/commission")
    public ResponseEntity<CollectorCommissionDTO> getCommissionByPeriod(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        CollectorCommissionDTO dto = service.getCommissionByPeriod(id, startDate, endDate);
        return ResponseEntity.ok(dto);
    }

}
