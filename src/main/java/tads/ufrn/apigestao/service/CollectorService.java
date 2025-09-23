package tads.ufrn.apigestao.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.Installment;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.collector.CollectorCommissionDTO;
import tads.ufrn.apigestao.domain.dto.collector.CollectorSalesAssignedDTO;
import tads.ufrn.apigestao.domain.dto.collector.CollectorSalesDTO;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentDTO;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentPaidDTO;
import tads.ufrn.apigestao.repository.CollectorRepository;
import tads.ufrn.apigestao.repository.InstallmentRepository;
import tads.ufrn.apigestao.repository.SaleRepository;

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

    public List<CollectorSalesDTO> getSalesByCollectorDTO(Long collectorId) {
        List<Sale> sales = saleService.getSalesByCollector(collectorId);

        return sales.stream().map(sale -> {
            Integer installmentsCount = sale.getInstallments();
            List<Installment> installments = installmentRepository.findBySaleId(sale.getId());

            List<InstallmentDTO> installmentDTOs = installments.stream()
                    .map(inst -> new InstallmentDTO(
                            inst.getId(),
                            inst.getDueDate(),
                            inst.getAmount(),
                            inst.isPaid()
                    ))
                    .toList();

            // Verificar se todas as parcelas estão pagas
            boolean fullyPaid = installmentDTOs.stream().allMatch(InstallmentDTO::isPaid);

            return new CollectorSalesDTO(
                    sale.getId(),
                    sale.getSaleDate(),
                    sale.getTotal(),
                    installmentsCount,
                    fullyPaid,
                    installmentDTOs
            );
        }).toList();
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

    public CollectorCommissionDTO getCurrentMonthCommission(Long collectorId) {
        Collector collector = repository.findById(collectorId)
                .orElseThrow(() -> new RuntimeException("Cobrador não encontrado"));

        YearMonth currentMonth = YearMonth.now();

        // Pegar todas as vendas do cobrador
        List<Sale> sales = saleService.getSalesByCollector(collectorId);

        // Somar todas as parcelas pagas do mês atual
        double totalCollected = sales.stream()
                .flatMap(sale -> installmentRepository.findBySaleId(sale.getId()).stream())
                .filter(inst -> inst.isPaid() && YearMonth.from(inst.getPaymentDate()).equals(currentMonth))
                .mapToDouble(Installment::getAmount)
                .sum();

        double commission = totalCollected * 0.01; // 1%

        return new CollectorCommissionDTO(
                collector.getId(),
                collector.getUser().getName(),
                currentMonth,
                commission
        );
    }
}
