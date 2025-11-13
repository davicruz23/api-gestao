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
import tads.ufrn.apigestao.domain.dto.installment.InstallmentPaidDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleCollectorDTO;
import tads.ufrn.apigestao.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectorService {

    private final CollectorRepository repository;
    private final SaleRepository saleRepository;
    private final SaleService saleService;
    private final InstallmentRepository installmentRepository;
    private final CommissionHistoryRepository commissionHistoryRepository;
    private final ApprovalLocationRepository  approvalLocationRepository;
    private final CollectionAttemptRepository collectionAttemptRepository;

    private static final double RADIUS_METERS = 500;

    public List<Collector> findAll() {
        return repository.findAll();
    }

    public List<Collector> findAlll() {
        List<Collector> collectors = repository.findAll();

        for (Collector collector : collectors) {
            if (collector.getSales() != null) {
                for (Sale sale : collector.getSales()) {
                    if (sale.getInstallments() != null) {
                        for (Installment installment : sale.getInstallmentsEntities()) {

                            if (installment.isPaid()) {
                                try {
                                    boolean valid = isAttemptWithinApprovalLocation(installment.getId());
                                    installment.setIsValid(valid);
                                } catch (Exception e) {
                                    installment.setIsValid(null);
                                }
                            } else {
                                installment.setIsValid(null);
                            }
                        }
                    }
                }
            }
        }

        return collectors;
    }

    public Collector findById(Long id) {
        return repository.findById(id).orElseThrow();
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
                        && inst.isCommissionable()
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

    public Map<String, List<SaleCollectorDTO>> findSalesByCollectorId(Long collectorId) {

        if (!repository.existsById(collectorId)) {
            throw new EntityNotFoundException("Collector com ID " + collectorId + " não encontrado.");
        }

        List<Sale> sales = saleRepository.findByCollectorIdWithPendingInstallments(collectorId);

        List<SaleCollectorDTO> saleDTOs = sales.stream()
                .map(SaleMapper::saleCollector)
                .toList();

        return saleDTOs.stream()
                .collect(Collectors.groupingBy(
                        sale -> {
                            if (sale.getClient() != null && sale.getClient().getAddress() != null) {
                                return sale.getClient().getAddress().getCity();
                            } else {
                                return "Cidade não informada";
                            }
                        }
                ));
    }


    public CollectorIdUserDTO getCollectorByUserId(Long userId) {
        Collector collector = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Collector não encontrado para o usuário: " + userId));

        return new CollectorIdUserDTO(collector.getId());
    }

    public boolean isAttemptWithinApprovalLocation(Long installmentId) {
        Installment installment = installmentRepository.findById(installmentId)
                .orElseThrow(() -> new RuntimeException("Parcela não encontrada"));

        Sale sale = installment.getSale();

        ApprovalLocation approvalLocation = approvalLocationRepository.findBySaleId(sale.getId())
                .orElseThrow(() -> new RuntimeException("Local de aprovação não encontrado"));

        // usa o repositório já existente que retorna List<CollectionAttempt>
        List<CollectionAttempt> attempts = collectionAttemptRepository.findPaidAttemptsByCollectorAndSale(
                sale.getCollector() != null ? sale.getCollector().getId() : null,
                sale.getId()
        );

        if (attempts == null || attempts.isEmpty()) {
            throw new RuntimeException("Tentativa de cobrança não encontrada");
        }

        // pega a mais recente (supondo campo attemptDate em CollectionAttempt)
        CollectionAttempt latestAttempt = attempts.stream()
                .max(Comparator.comparing(CollectionAttempt::getAttemptAt))
                .orElseThrow(() -> new RuntimeException("Nenhuma tentativa válida encontrada"));

        double distance = distanceInMeters(
                approvalLocation.getLatitude(),
                approvalLocation.getLongitude(),
                latestAttempt.getLatitude(),
                latestAttempt.getLongitude()
        );

        return distance <= RADIUS_METERS;
    }


    private double distanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371000; // metros

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) *
                        Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
