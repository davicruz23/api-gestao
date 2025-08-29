package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.sale.UpsertSaleDTO;
import tads.ufrn.apigestao.enums.PaymentType;
import tads.ufrn.apigestao.repository.InstallmentRepository;
import tads.ufrn.apigestao.repository.SaleRepository;

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

    /*public Product update(ProductDTO product){
        Product productId = repository.findById(product.getId())
                .orElseThrow(()-> new RuntimeException("Produto não encontrado"));

        Product p = new Product();
        p.setName(product.getName());
        p.setBrand(product.getBrand());
        p.setAmount(product.getAmount());

        return repository.save(p);
    }*/

    public void deleteById(Long id){
        Sale sale = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Sale not found"));
        repository.save(sale);

    }

    public Sale approvePreSale(Long preSaleId, Inspector inspector, PaymentType paymentMethod, int installments) {
        PreSale preSale = preSaleService.approvePreSale(preSaleId, inspector);

        Sale sale = new Sale();
        sale.setPreSale(preSale);
        sale.setSaleDate(LocalDateTime.now());
        sale.setNumberSale(UUID.randomUUID().toString());
        sale.setInstallments(installments);
        sale.setPaymentMethod(paymentMethod);

        double total = preSale.getItems().stream()
                .mapToDouble(item -> item.getProduct().getValue() * item.getQuantity())
                .sum();
        sale.setTotal(total);

        repository.save(sale);

        generateInstallments(sale);

        return sale;
    }


//    public void rejectPreSale(Long preSaleId) {
//        preSaleService.rejectPreSale(preSaleId);
//    }

    public void generateInstallments(Sale sale) {
        double installmentValue = sale.getTotal() / sale.getInstallments();
        List<Installment> installments = new ArrayList<>();

        LocalDateTime firstDueDate = sale.getSaleDate().plusDays(30);

        for (int i = 0; i < sale.getInstallments(); i++) {
            Installment inst = new Installment();
            inst.setSale(sale);
            inst.setAmount(installmentValue);
            inst.setDueDate(firstDueDate.plusMonths(i));
            installments.add(inst);
        }

        installmentRepository.saveAll(installments);
    }
}
