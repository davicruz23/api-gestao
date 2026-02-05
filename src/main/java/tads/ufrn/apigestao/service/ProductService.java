package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tads.ufrn.apigestao.domain.ChargingItem;
import tads.ufrn.apigestao.domain.Product;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.domain.dto.product.UpsertProductDTO;
import tads.ufrn.apigestao.domain.dto.product.UptadeProductDTO;
import tads.ufrn.apigestao.enums.ProductStatus;
import tads.ufrn.apigestao.exception.BusinessException;
import tads.ufrn.apigestao.exception.ResourceNotFoundException;
import tads.ufrn.apigestao.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final UserService userService;
    private final ModelMapper mapper;

    public List<Product> findAll(){
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAll(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        String filter = (name == null || name.isBlank()) ? null : name.trim();

        Page<Product> result = repository.findAllByFilter(filter, pageable);

        return result.map(this::toDTO);
    }

    private ProductDTO toDTO(Product p) {
        return new ProductDTO(
                p.getId(),
                p.getName(),
                p.getBrand(),
                p.getAmount(),
                p.getValue(),
                resolveStatus(p.getAmount())
        );
    }


    private int resolveStatus(Integer amount) {

        if (amount == 0) {
            return ProductStatus.ZERADO.getValue();
        }

        if (amount <= 10) {
            return ProductStatus.MUITOPOUCO.getValue();
        }

        if (amount <= 30) {
            return ProductStatus.POUCO.getValue();
        }

        return ProductStatus.DISPONIVEL.getValue();
    }

    public Page<Product> index(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public Product findById(Long id) {
        Optional<Product> product = repository.findById(id);
        return product.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado!"));
    }

    public Product store(UpsertProductDTO product){

        User admin = userService.findUserById(1L);
        Product prod =  mapper.map(product, Product.class);
        prod.setCreatedBy(admin);
        prod.create();

        return repository.save(prod);
    }

    public Product updateProduct(UptadeProductDTO productDto) {
        Product existingProduct = repository.findById(productDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado!"));

        existingProduct.setAmount(productDto.getAmount());
        existingProduct.setValue(productDto.getValue());

        return repository.save(existingProduct);
    }

    public void deleteById(Long id){
        Product product = repository.findById(id)
                        .orElseThrow(()-> new ResourceNotFoundException("Produto não encontrado!"));
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
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: "+ productId));
            product.setAmount(product.getAmount() + quantity);
            repository.save(product);
        } catch (Exception e) {
            throw new BusinessException("Erro ao devolver estoque");
        }
    }
}
