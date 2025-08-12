package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.Product;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.domain.dto.product.UpsertProductDTO;
import tads.ufrn.apigestao.enums.ProductStatus;
import tads.ufrn.apigestao.enums.UserType;
import tads.ufrn.apigestao.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ModelMapper mapper;

    public List<Product> findAll(){
        return repository.findAll();
    }

    public Product findUserById(Long id) {
        Optional<Product> product = repository.findById(id);
        return product.orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public Product store(UpsertProductDTO product){
        Product prod =  mapper.map(product, Product.class);

        if (product.getStatus() != null) {
            prod.setStatus(ProductStatus.fromValue(product.getStatus()));
        }
        prod.create();

        return repository.save(prod);
    }

    public Product update(ProductDTO product){
        Product productId = repository.findById(product.getId())
                .orElseThrow(()-> new RuntimeException("Produto não encontrado"));

        Product p = new Product();
        p.setName(product.getName());
        p.setBrand(product.getBrand());
        p.setAmount(product.getAmount());

        return repository.save(p);
    }

    public void deleteById(Long id){
        Product product = repository.findById(id)
                        .orElseThrow(()-> new NotFoundException("Product not found"));
        product.delete();
        repository.save(product);

    }
}
