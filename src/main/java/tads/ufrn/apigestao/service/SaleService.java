package tads.ufrn.apigestao.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.sale.SalesByCityDTO;
import tads.ufrn.apigestao.domain.dto.sale.UpsertSaleDTO;
import tads.ufrn.apigestao.enums.PaymentType;
import tads.ufrn.apigestao.repository.ApprovalLocationRepository;
import tads.ufrn.apigestao.repository.InstallmentRepository;
import tads.ufrn.apigestao.repository.PreSaleRepository;
import tads.ufrn.apigestao.repository.SaleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
                               Double cashPaid, Double latitude, Double longitude) {

        PreSale preSale = preSaleService.approvePreSale(preSaleId, inspector);

        Sale sale = new Sale();
        sale.setPreSale(preSale);
        sale.setSaleDate(LocalDate.now());
        sale.setNumberSale(UUID.randomUUID().toString());
        sale.setInstallments(installments);
        sale.setPaymentMethod(paymentMethod);

        double total = preSale.getItems().stream()
                .mapToDouble(item -> item.getProduct().getValue() * item.getQuantity())
                .sum();
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

        if (cashPaid != null && cashPaid > 0) {
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


        double remaining = total - (cashPaid != null ? cashPaid : 0.0);
        if (remaining > 0 && installments > 0) {
            double installmentValue = remaining / installments;
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
}
