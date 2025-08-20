package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.preSale.UpsertPreSaleDTO;
import tads.ufrn.apigestao.repository.PreSaleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreSaleService {

    private final PreSaleRepository repository;
    private final ModelMapper mapper;

    public List<PreSale> findAll(){
        return repository.findAll();
    }

    public PreSale findById(Long id) {
        Optional<PreSale> preSale = repository.findById(id);
        return preSale.orElseThrow(() -> new NotFoundException("User not found"));
    }

    public PreSale store(UpsertPreSaleDTO preSale) {
        return repository.save(mapper.map(preSale, PreSale.class));
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
        PreSale client = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product not found"));
        repository.save(client);

    }

    public PreSale approvePreSale(Long preSaleId, Inspector inspector) {
        PreSale preSale = findById(preSaleId);
        preSale.setApproved(true);
        preSale.setInspector(inspector);
        return repository.save(preSale);
    }

    @Transactional
    public void rejectPreSale(Long preSaleId, Inspector inspector) {
        PreSale preSale = findById(preSaleId);

        preSale.setApproved(false);
        preSale.setInspector(inspector);

        // Para cada item da pré-venda
        for (PreSaleItem item : preSale.getItems()) {
            int quantity = item.getQuantity();

            ChargingItem chargingItem = item.getChargingItem();
            if (chargingItem != null) {
                // devolve a quantidade reservada
                chargingItem.setQuantity(chargingItem.getQuantity() + quantity);
            }
            // não mexe no Product.amount global
        }

        repository.save(preSale);
    }




}
