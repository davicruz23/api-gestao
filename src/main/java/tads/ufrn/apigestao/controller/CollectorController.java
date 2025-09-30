package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tads.ufrn.apigestao.controller.mapper.CollectorMapper;
import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.Installment;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.collector.*;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorIdUserDTO;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentPaidDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleCollectorDTO;
import tads.ufrn.apigestao.service.CollectorService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/collector")
public class CollectorController {

    private final CollectorService service;

    @PostMapping("/{collectorId}/assign/{city}")
    public ResponseEntity<CollectorSalesAssignedDTO> assignSalesByCity(
            @PathVariable Long collectorId,
            @PathVariable String city
    ) {
        System.out.println("entrei no metodo de cidade");
        return ResponseEntity.ok(service.assignSalesByCity(collectorId, city));
    }

    @GetMapping("/{collectorId}/salesssssss")
    public ResponseEntity<List<Sale>> getSales(@PathVariable Long collectorId) {
        return ResponseEntity.ok(service.getSales(collectorId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CollectorDTO>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(CollectorMapper::mapper).toList());
    }

//    @GetMapping("/{collectorId}/sales")
//    public ResponseEntity<List<CollectorSalesDTO>> getCollectorSales(@PathVariable Long collectorId) {
//        List<CollectorSalesDTO> salesDTO = service.getSalesByCollectorDTO(collectorId);
//        return ResponseEntity.ok(salesDTO);
//    }

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
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "false") boolean saveHistory) {

        CollectorCommissionDTO dto = service.getCommissionByPeriod(id, startDate, endDate, saveHistory);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{collectorId}/sales")
    public ResponseEntity<List<SaleCollectorDTO>> getSalesByCollector(@PathVariable Long collectorId) {
        List<SaleCollectorDTO> sales = service.findSalesByCollectorId(collectorId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<CollectorIdUserDTO> getCollectorByUserId(@PathVariable Long userId) {
        CollectorIdUserDTO dto = service.getCollectorByUserId(userId);
        System.out.println("chamei o endpoint de seller user: "+ dto.toString());
        return ResponseEntity.ok(dto);
    }

}
