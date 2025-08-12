package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.PreSale;
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
}
