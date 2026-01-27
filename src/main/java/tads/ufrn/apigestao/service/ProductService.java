package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.ChargingItem;
import tads.ufrn.apigestao.domain.Product;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.domain.dto.product.UpsertProductDTO;
import tads.ufrn.apigestao.domain.dto.product.UptadeProductDTO;
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

    public Page<Product> index(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public Product findById(Long id) {
        Optional<Product> product = repository.findById(id);
        return product.orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public Product store(UpsertProductDTO product){
        Product prod =  mapper.map(product, Product.class);
        prod.create();

        return repository.save(prod);
    }

    public Product updateProduct(UptadeProductDTO productDto) {
        Product existingProduct = repository.findById(productDto.getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        existingProduct.setAmount(productDto.getAmount());
        existingProduct.setValue(productDto.getValue());

        return repository.save(existingProduct);
    }

    public void deleteById(Long id){
        Product product = repository.findById(id)
                        .orElseThrow(()-> new NotFoundException("Product not found"));
        product.delete();
        repository.save(product);

    }

    public void saveAllFromChargingItems(List<ChargingItem> items) {
        List<Product> products = items.stream()
                .map(ChargingItem::getProduct)
                .toList();
        repository.saveAll(products);
    }

    public void returnStock(Long productId, int quantity) {
        try {
            Product product = repository.findById(productId)
                    .orElseThrow(() -> new NotFoundException("Product not found with id " + productId));
            product.setAmount(product.getAmount() + quantity);
            repository.save(product);
        } catch (Exception e) {
            System.err.println("Erro ao devolver estoque do produto " + productId + ": " + e.getMessage());
            throw new RuntimeException("Erro ao devolver estoque", e);
        }
    }
}
