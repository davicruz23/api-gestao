package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tads.ufrn.apigestao.domain.dto.collector.CollectorCommissionDTO;
import tads.ufrn.apigestao.domain.dto.dashboard.*;
import tads.ufrn.apigestao.service.DashboardService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/count/sales")
    public ResponseEntity<DashboardSaleDTO> getSalesDashboard(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        DashboardSaleDTO dashboard = dashboardService.getSalesDashboard(startDate, endDate);
        return ResponseEntity.ok(dashboard);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/count/preSales/status")
    public ResponseEntity<Map<String, Long>> getSalesStatusCount() {
        Map<String, Long> statusCount = dashboardService.getSalesStatusCount();
        return ResponseEntity.ok(statusCount);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/count/collector")
    public ResponseEntity<List<CollectorChargeSummaryDTO>> getChargeSummary(@RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {

        List<CollectorChargeSummaryDTO> summary = dashboardService.getCollectorsChargeSummary(startDate, endDate);
        return ResponseEntity.ok(summary);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/commissions/collector")
    public ResponseEntity<List<CollectorCommissionDTO>> getAllCommissions(@RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {

        List<CollectorCommissionDTO> commissions = dashboardService.getAllCommissionsByPeriod(startDate, endDate);
        return ResponseEntity.ok(commissions);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/count/products-sold")
    public ResponseEntity<List<DashboardProductSalesDTO>> getTotalProductsSold(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DashboardProductSalesDTO> result = dashboardService.getTotalProductsSold(startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/count/total-cobrado")
    public ResponseEntity<DashboardTotalCobradoDTO> getTotalCobrado() {
        return ResponseEntity.ok(dashboardService.getTotalCobrado());
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/count/total-clients")
    public ResponseEntity<DashboardTotalClientsDTO> getTotalClients() {
        DashboardTotalClientsDTO dto = dashboardService.getTotalClients();
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/sales-per-month")
    public List<DashboardSalesPerMonthDTO> getSalesPerMonth(@RequestParam(defaultValue = "6") int meses) {
        return dashboardService.getSalesPerMonth(meses);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/count/cities")
    public ResponseEntity<Long> getDistinctCityCount() {
        Long count = dashboardService.getDistinctCityCount();
        return ResponseEntity.ok(count);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/count/chargings")
    public ResponseEntity<Long> getChargingCount() {
        Long count = dashboardService.getCountChargins();
        return ResponseEntity.ok(count);
    }
}
