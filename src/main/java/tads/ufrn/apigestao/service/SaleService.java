package tads.ufrn.apigestao.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.sale.CitySalesDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleItemDTO;
import tads.ufrn.apigestao.domain.dto.sale.SalesByCityDTO;
import tads.ufrn.apigestao.domain.dto.sale.UpsertSaleDTO;
import tads.ufrn.apigestao.enums.PaymentType;
import tads.ufrn.apigestao.repository.ApprovalLocationRepository;
import tads.ufrn.apigestao.repository.InstallmentRepository;
import tads.ufrn.apigestao.repository.PreSaleRepository;
import tads.ufrn.apigestao.repository.SaleRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository repository;
    private final PreSaleService preSaleService;
    private final InstallmentRepository installmentRepository;
    private final ApprovalLocationRepository approvalLocationRepository;
    private final ModelMapper mapper;

    public List<Sale> findAll(){
        return repository.findAll();
    }

    public Sale findById(Long id) {
        Optional<Sale> sale = repository.findById(id);
        return sale.orElseThrow(() -> new NotFoundException("Sale not found"));
    }

    public Sale store(UpsertSaleDTO sale) {
        return repository.save(mapper.map(sale, Sale.class));
    }

    public void deleteById(Long id){
        Sale sale = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Sale not found"));
        repository.save(sale);

    }

    @Transactional
    public Sale approvePreSale(Long preSaleId, Inspector inspector,
                               PaymentType paymentMethod, int installments,
                               BigDecimal cashPaid, Double latitude, Double longitude) {

        PreSale preSale = preSaleService.approvePreSale(preSaleId, inspector);

        Sale sale = new Sale();
        sale.setPreSale(preSale);
        sale.setSaleDate(LocalDate.now());
        sale.setNumberSale(UUID.randomUUID().toString());
        sale.setInstallments(installments);
        sale.setPaymentMethod(paymentMethod);

        BigDecimal total = preSale.getItems().stream()
                .map(item ->
                        item.getProduct().getValue()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        sale.setTotal(total);
        repository.save(sale);

        ApprovalLocation location = new ApprovalLocation();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setCapturedAt(LocalDateTime.now());
        location.setInspector(inspector.getUser());
        location.setSale(sale);
        approvalLocationRepository.save(location);

        sale.setApprovalLocation(location);

        if (cashPaid != null && cashPaid.compareTo(BigDecimal.ZERO) > 0) {
            Installment upfront = new Installment();
            upfront.setSale(sale);
            upfront.setAmount(cashPaid);
            upfront.setPaid(true);
            upfront.setPaymentDate(LocalDateTime.now());
            upfront.setDueDate(LocalDate.now());
            upfront.setPaymentType(PaymentType.CASH);
            upfront.setCommissionable(false);

            installmentRepository.save(upfront);
        }

        BigDecimal paid = cashPaid != null ? cashPaid : BigDecimal.ZERO;
        BigDecimal remaining = total.subtract(paid);

        if (remaining.compareTo(BigDecimal.ZERO) > 0 && installments > 0) {

            BigDecimal installmentValue = remaining.divide(
                    BigDecimal.valueOf(installments),
                    2,
                    RoundingMode.HALF_UP
            );

            LocalDate firstDueDate = sale.getSaleDate().plusDays(30);

            for (int i = 0; i < installments; i++) {
                Installment inst = new Installment();
                inst.setSale(sale);
                inst.setAmount(installmentValue);
                inst.setDueDate(firstDueDate.plusMonths(i));
                inst.setPaid(false);
                inst.setPaymentType(PaymentType.PARCEL);

                installmentRepository.save(inst);
            }
        }

        return sale;
    }

    public List<SalesByCityDTO> getSalesGroupedByCity() {
        return repository.countSaleByCity();
    }

    public List<Sale> getSalesByCollector(Long collectorId) {
        return repository.findByCollectorId(collectorId);
    }

    public List<CitySalesDTO> salesGroupedByCityAssignment() {
        List<Object[]> rows = repository.findSalesDetailedWithoutCollector();

        Map<String, List<SaleItemDTO>> grouped = new LinkedHashMap<>();

        for (Object[] row : rows) {

            String city = (String) row[0];

            SaleItemDTO sale = new SaleItemDTO(
                    ((Number) row[1]).longValue(),
                    ((Number) row[2]).longValue(),
                    ((Number) row[3]).longValue(),
                    (String) row[4],
                    city,
                    (String) row[5],
                    (BigDecimal) row[6]
            );

            grouped.computeIfAbsent(city, c -> new ArrayList<>()).add(sale);
        }

        List<CitySalesDTO> result = new ArrayList<>();

        for (var entry : grouped.entrySet()) {
            result.add(new CitySalesDTO(
                    entry.getKey(),
                    entry.getValue().size(),
                    entry.getValue()
            ));
        }

        return result;
    }

    @Transactional
    public void assignSalesToCollector(Long collectorId, List<Long> saleIds) {
        if (saleIds == null || saleIds.isEmpty()) return;

        repository.assignCollector(collectorId, saleIds);
    }
}
