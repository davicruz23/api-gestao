package tads.ufrn.apigestao.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tads.ufrn.apigestao.controller.mapper.SaleMapper;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.collector.CollectorCommissionDTO;
import tads.ufrn.apigestao.domain.dto.collector.CollectorIdUserDTO;
import tads.ufrn.apigestao.domain.dto.collector.CollectorSalesAssignedDTO;
import tads.ufrn.apigestao.domain.dto.collector.CollectorSalesDTO;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorIdUserDTO;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentDTO;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentPaidDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleCollectorDTO;
import tads.ufrn.apigestao.repository.CollectorRepository;
import tads.ufrn.apigestao.repository.CommissionHistoryRepository;
import tads.ufrn.apigestao.repository.InstallmentRepository;
import tads.ufrn.apigestao.repository.SaleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectorService {

    private final CollectorRepository repository;
    private final SaleRepository saleRepository;
    private final SaleService saleService;
    private final InstallmentRepository installmentRepository;
    private final CommissionHistoryRepository commissionHistoryRepository;

    public List<Collector> findAll() {
        return repository.findAll();
    }

    @Transactional
    public CollectorSalesAssignedDTO assignSalesByCity(Long collectorId, String city) {
        System.out.println("entrei no metodo");
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

    @Transactional
    public InstallmentPaidDTO markAsPaid(Long installmentId) {
        Installment installment = installmentRepository.findById(installmentId)
                .orElseThrow(() -> new RuntimeException("Parcela não encontrada"));

        if (installment.isPaid()) {
            throw new RuntimeException("Parcela já está marcada como paga");
        }

        installment.setPaid(true);
        installment.setPaymentDate(LocalDateTime.now());
        installmentRepository.save(installment);

        return new InstallmentPaidDTO(
                installment.getId(),
                installment.getDueDate(),
                installment.getAmount(),
                installment.isPaid(),
                installment.getPaymentDate()
        );
    }

    public CollectorCommissionDTO getCommissionByPeriod(Long collectorId, LocalDate startDate, LocalDate endDate, boolean saveHistory) {

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("A data final não pode ser anterior à data inicial.");
        }
        Collector collector = repository.findById(collectorId)
                .orElseThrow(() -> new RuntimeException("Cobrador não encontrado"));
        List<Sale> sales = saleService.getSalesByCollector(collectorId);
        double totalCollected = sales.stream()
                .flatMap(sale -> installmentRepository.findBySaleId(sale.getId()).stream())
                .filter(inst -> inst.isPaid()
                        && inst.getPaymentDate() != null
                        && !inst.getPaymentDate().isBefore(startDate.atStartOfDay())
                        && !inst.getPaymentDate().isAfter(endDate.atTime(23, 59, 59)))
                .mapToDouble(Installment::getAmount)
                .sum();
        double commission = totalCollected * 0.01;

        if (saveHistory) {
            CommissionHistory history = new CommissionHistory();
            history.setCollector(collector);
            history.setGeneratedAt(LocalDateTime.now());
            history.setStartDate(startDate);
            history.setEndDate(endDate);
            history.setAmount(commission);
            commissionHistoryRepository.save(history);
        }

        return new CollectorCommissionDTO(
                collector.getId(),
                collector.getUser().getName(),
                startDate,
                endDate,
                commission
        );
    }

    public List<SaleCollectorDTO> findSalesByCollectorId(Long collectorId) {

        if (!repository.existsById(collectorId)) {
            throw new EntityNotFoundException("Collector com ID " + collectorId + " não encontrado.");
        }

        List<Sale> sales = saleRepository.findByCollectorIdWithPendingInstallments(collectorId);

        return sales.stream()
                .map(SaleMapper::saleCollector)
                .toList();
    }

    public CollectorIdUserDTO getCollectorByUserId(Long userId) {
        Collector collector = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Collector não encontrado para o usuário: " + userId));

        return new CollectorIdUserDTO(collector.getId());
    }
}
