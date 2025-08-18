package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.controller.mapper.ChargingMapper;
import tads.ufrn.apigestao.domain.Charging;
import tads.ufrn.apigestao.domain.ChargingItem;
import tads.ufrn.apigestao.domain.Product;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.charging.ChargingDTO;
import tads.ufrn.apigestao.domain.dto.charging.UpsertChargingDTO;
import tads.ufrn.apigestao.domain.dto.chargingItem.UpsertChargingItemDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.repository.ChargingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChangingService {

    private final ChargingRepository repository;
    private final UserService userService;
    private final ProductService productService;
    private final ModelMapper mapper;

    public List<Charging> findAll() {
        return repository.findAll();
    }

    public Charging findById(Long id) {
        Optional<Charging> obj = repository.findById(id);
        return obj.orElseThrow(() -> new NotFoundException("ChargingMapper não encontrado!"));
    }

    //@Transactional
    public Charging store(UpsertChargingDTO chargingDTO) {

        User user = userService.findUserById(2L);

        Charging charging = new Charging();
        charging.setDescription(chargingDTO.getDescription());
        charging.setDate(chargingDTO.getDate());
        charging.setCreatedAt(LocalDate.now());
        charging.setUser(user);

        for (UpsertChargingItemDTO itemDTO : chargingDTO.getItems()) {
            Product product = productService.findById(itemDTO.getProductId());

            if (product.getAmount() < itemDTO.getQuantity()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + product.getId());
            }
            product.setAmount(product.getAmount() - itemDTO.getQuantity());

            charging.addItem(product, itemDTO.getQuantity());
        }

        Charging savedCharging = repository.save(charging);
        productService.saveAllFromChargingItems(savedCharging.getItems());

        return savedCharging;
    }

    public Charging update(UpsertChargingDTO model) {
        return repository.save(mapper.map(model, Charging.class));
    }

    public void deleteById(Long id){
        Charging charging = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Charging not found"));
        charging.delete();
        repository.save(charging);

    }
}
