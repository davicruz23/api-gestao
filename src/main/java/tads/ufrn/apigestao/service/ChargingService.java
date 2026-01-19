package tads.ufrn.apigestao.service;

import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
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
import tads.ufrn.apigestao.repository.ChargingRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargingService {

    private final ChargingRepository repository;
    private final UserService userService;
    private final ProductService productService;
    private final ChargingItemService chargingItemService;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<ChargingDTO> findAll() {
        return repository.findAllWithItems()
                .stream()
                .map(ChargingMapper::mapper)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChargingDTO> findCurrent() {
        return repository.findAllCurrentWithItems()
                .stream()
                .map(ChargingMapper::mapper)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChargingDTO findById(Long id) {
        Charging charging = repository.findByIdWithItems(id)
                .orElseThrow(() -> new NotFoundException("Charging não encontrado"));

        return ChargingMapper.mapper(charging);
    }

    @Transactional(readOnly = true)
    public Charging findEntityById(Long id) {
        return repository.findByIdWithItems(id)
                .orElseThrow(() -> new NotFoundException("Charging não encontrado"));
    }

    @jakarta.transaction.Transactional
    public Charging store(UpsertChargingDTO chargingDTO) {

        User user = userService.findUserById(2L);

        List<Charging> previousChargings = repository.findAllByUserIdAndDeletedAtIsNull(user.getId());
        for (Charging oldCharging : previousChargings) {
            deleteById(oldCharging.getId());
        }

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

    @jakarta.transaction.Transactional
    public void deleteById(Long id) {
        Charging charging = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Charging not found"));

        for (ChargingItem item : charging.getItems()) {
            productService.returnStock(item.getProduct().getId(), item.getQuantity());
        }

        chargingItemService.markItemsAsDeletedByChargingId(id);

        charging.delete();
        repository.save(charging);
    }

}
