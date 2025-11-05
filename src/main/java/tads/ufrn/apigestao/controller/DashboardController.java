package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tads.ufrn.apigestao.domain.dto.collector.CollectorCommissionDTO;
import tads.ufrn.apigestao.domain.dto.dashboard.CollectorChargeSummaryDTO;
import tads.ufrn.apigestao.domain.dto.dashboard.DashboardProductSalesDTO;
import tads.ufrn.apigestao.domain.dto.dashboard.DashboardSaleDTO;
import tads.ufrn.apigestao.service.DashboardService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/count/sales")
    public ResponseEntity<DashboardSaleDTO> getSalesDashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        DashboardSaleDTO dashboard = dashboardService.getSalesDashboard(startDate, endDate);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/count/preSales/status")
    public ResponseEntity<Map<String, Long>> getSalesStatusCount() {
        Map<String, Long> statusCount = dashboardService.getSalesStatusCount();
        return ResponseEntity.ok(statusCount);
    }

    @GetMapping("/count/collector")
    public ResponseEntity<List<CollectorChargeSummaryDTO>> getChargeSummary(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        List<CollectorChargeSummaryDTO> summary = dashboardService.getCollectorsChargeSummary(startDate, endDate);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/commissions/collector")
    public ResponseEntity<List<CollectorCommissionDTO>> getAllCommissions(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        List<CollectorCommissionDTO> commissions = dashboardService.getAllCommissionsByPeriod(startDate, endDate);
        return ResponseEntity.ok(commissions);
    }

    @GetMapping("/count/products-sold")
    public ResponseEntity<List<DashboardProductSalesDTO>> getTotalProductsSold(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<DashboardProductSalesDTO> result = dashboardService.getTotalProductsSold(startDate, endDate);
        return ResponseEntity.ok(result);
    }
}
