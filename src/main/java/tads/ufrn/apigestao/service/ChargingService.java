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
import tads.ufrn.apigestao.domain.dto.charging.AddChargingItemDTO;
import tads.ufrn.apigestao.domain.dto.charging.ChargingDTO;
import tads.ufrn.apigestao.domain.dto.charging.UpdateChargingItemDTO;
import tads.ufrn.apigestao.domain.dto.charging.UpsertChargingDTO;
import tads.ufrn.apigestao.domain.dto.chargingItem.UpsertChargingItemDTO;
import tads.ufrn.apigestao.repository.ChargingRepository;
import tads.ufrn.apigestao.repository.ProductRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargingService {

    private final ChargingRepository repository;
    private final UserService userService;
    private final ProductService productService;
    private final ChargingItemService chargingItemService;
    private final ProductRepository productRepository;

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

    @Transactional
    public Charging store(UpsertChargingDTO chargingDTO) {

        User userCharging = userService.findUserById(1L);

        Charging charging = repository.findFirstBy()
                .orElseGet(Charging::new);

        for (UpsertChargingItemDTO itemDTO : chargingDTO.getItems()) {

            Product product = productService.findById(itemDTO.getProductId());
            int newQuantity = itemDTO.getQuantity();

            ChargingItem item = charging.getItems()
                    .stream()
                    .filter(i -> i.getProduct().getId().equals(product.getId()))
                    .findFirst()
                    .orElse(null);

            if (item != null) {

                int diff = newQuantity - item.getQuantity();

                if (diff > 0 && product.getAmount() < diff) {
                    throw new RuntimeException(
                            "Estoque insuficiente para o produto: " + product.getId()
                    );
                }

                product.setAmount(product.getAmount() - diff);
                item.setQuantity(newQuantity);

            } else {

                if (product.getAmount() < newQuantity) {
                    throw new RuntimeException(
                            "Estoque insuficiente para o produto: " + product.getId()
                    );
                }

                product.setAmount(product.getAmount() - newQuantity);
                charging.addItem(product, newQuantity);
                charging.setDescription(chargingDTO.getDescription());
                charging.setDate(LocalDate.now());
                charging.setUser(userCharging);
                charging.setCreatedAt(LocalDate.now());
                charging.setDescription("MERCADORIAS");
            }
        }

        Charging savedCharging = repository.save(charging);
        productService.saveAllFromChargingItems(savedCharging.getItems());

        return savedCharging;
    }

    @Transactional
    public ChargingDTO addProductsToCharging(List<AddChargingItemDTO> itemsToAdd) {

        System.out.println("Print: " + itemsToAdd);

        Charging charging = repository.findFirstBy()
                .orElseThrow(() -> new RuntimeException("Carregamento não encontrado"));

        for (AddChargingItemDTO dto : itemsToAdd) {

            if (dto.quantity() <= 0) {
                throw new RuntimeException("Quantidade inválida para o produto " + dto.productId());
            }

            Product product = productService.findById(dto.productId());

            if (product.getAmount() < dto.quantity()) {
                throw new RuntimeException(
                        "Estoque insuficiente para o produto: " + dto.productId()
                );
            }

            ChargingItem item = charging.getItems()
                    .stream()
                    .filter(i -> i.getProduct().getId().equals(dto.productId()))
                    .findFirst()
                    .orElse(null);

            product.setAmount(product.getAmount() - dto.quantity());
            productRepository.save(product);

            if (item != null) {
                item.setQuantity(item.getQuantity() + dto.quantity());
            } else {
                charging.addItem(product, dto.quantity());

            }
        }

        repository.save(charging);

        return ChargingMapper.mapper(charging);
    }

    @Transactional
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
