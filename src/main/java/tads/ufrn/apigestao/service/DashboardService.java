package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.Installment;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.collector.CollectorCommissionDTO;
import tads.ufrn.apigestao.domain.dto.dashboard.CollectorChargeSummaryDTO;
import tads.ufrn.apigestao.domain.dto.dashboard.DashboardProductSalesDTO;
import tads.ufrn.apigestao.domain.dto.dashboard.DashboardSaleDTO;
import tads.ufrn.apigestao.enums.PreSaleStatus;
import tads.ufrn.apigestao.repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SaleRepository saleRepository;
    private final PreSaleRepository preSaleRepository;
    private final CollectorRepository collectorRepository;
    private final InstallmentRepository installmentRepository;
    private final PreSaleItemRepository preSaleItemRepository;
    private final SaleService saleService;

    public DashboardSaleDTO getSalesDashboard(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            LocalDate hoje = LocalDate.now();
            startDate = hoje.withDayOfMonth(1);
            endDate = hoje.withDayOfMonth(hoje.lengthOfMonth());
        }

        List<Sale> sales = saleRepository.findSalesByDateRange(startDate, endDate);

        Long totalVendas = (long) sales.size();

        BigDecimal totalValor = sales.stream()
                .filter(s -> s.getTotal() != null)
                .map(s -> BigDecimal.valueOf(s.getTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate startPrev = startDate.minusMonths(1);
        LocalDate endPrev = startPrev.withDayOfMonth(startPrev.lengthOfMonth());

        List<Sale> prevSales = saleRepository.findSalesByDateRange(startPrev, endPrev);
        BigDecimal totalValorAnterior = prevSales.stream()
                .filter(s -> s.getTotal() != null)
                .map(s -> BigDecimal.valueOf(s.getTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double percentual = 0.0;
        if (totalValorAnterior.compareTo(BigDecimal.ZERO) > 0) {
            percentual = totalValor.subtract(totalValorAnterior)
                    .divide(totalValorAnterior, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        }

        return new DashboardSaleDTO(totalVendas, totalValor, percentual);
    }

    public Map<String, Long> getSalesStatusCount() {
        Long pending = preSaleRepository.countByStatus(PreSaleStatus.fromValue(1));
        Long approved = preSaleRepository.countByStatus(PreSaleStatus.fromValue(2));
        Long rejected = preSaleRepository.countByStatus(PreSaleStatus.fromValue(3));

        Map<String, Long> result = new HashMap<>();
        result.put("PENDENTE", pending);
        result.put("APROVADA", approved);
        result.put("RECUSADA", rejected);
        return result;
    }

    public List<CollectorChargeSummaryDTO> getCollectorsChargeSummary(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = collectorRepository.findCollectorsWithChargeCountAndTotal(startDate, endDate);

        return results.stream().map(result -> {
            String collectorName = (String) result[0];
            Long chargeCount = result[1] != null ? ((Number) result[1]).longValue() : 0L;
            Double totalAmountDouble = (Double) result[2];
            BigDecimal totalAmount = totalAmountDouble != null ? BigDecimal.valueOf(totalAmountDouble) : BigDecimal.ZERO;

            return new CollectorChargeSummaryDTO(collectorName, chargeCount, totalAmount);
        }).collect(Collectors.toList());
    }

    public List<CollectorCommissionDTO> getAllCommissionsByPeriod(LocalDate startDate, LocalDate endDate) {
        List<Collector> collectors = collectorRepository.findAll();

        return collectors.stream().map(collector -> {
            List<Sale> sales = saleService.getSalesByCollector(collector.getId());
            double totalCollected = sales.stream()
                    .flatMap(sale -> installmentRepository.findBySaleId(sale.getId()).stream())
                    .filter(inst -> inst.isPaid()
                            && inst.isCommissionable()
                            && inst.getPaymentDate() != null
                            && (startDate == null || !inst.getPaymentDate().isBefore(startDate.atStartOfDay()))
                            && (endDate == null || !inst.getPaymentDate().isAfter(endDate.atTime(23, 59, 59))))
                    .mapToDouble(Installment::getAmount)
                    .sum();
            double commission = totalCollected * 0.01;

            return new CollectorCommissionDTO(
                    collector.getId(),
                    collector.getUser().getName(),
                    startDate,
                    endDate,
                    commission
            );
        }).collect(Collectors.toList());
    }

    public List<DashboardProductSalesDTO> getTotalProductsSold(LocalDate startDate, LocalDate endDate) {
        return preSaleItemRepository.findTotalProductsSoldByDateRange(startDate, endDate);
    }
}
