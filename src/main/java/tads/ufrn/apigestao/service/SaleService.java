package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.Client;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.client.UpsertClientDTO;
import tads.ufrn.apigestao.domain.dto.sale.UpsertSaleDTO;
import tads.ufrn.apigestao.repository.ClientRepository;
import tads.ufrn.apigestao.repository.SaleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository repository;
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
}
