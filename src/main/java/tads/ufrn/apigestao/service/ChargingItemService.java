package tads.ufrn.apigestao.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.Charging;
import tads.ufrn.apigestao.domain.ChargingItem;
import tads.ufrn.apigestao.domain.PreSale;
import tads.ufrn.apigestao.repository.ChargingItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChargingItemService {

    private final ChargingItemRepository repository;

    public List<ChargingItem> findAll() {
        return repository.findAll();
    }

    public ChargingItem findById(Long id) {
        Optional<ChargingItem> preSale = repository.findById(id);
        return preSale.orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional
    public void markItemsAsDeletedByChargingId(Long chargingId) {
        List<ChargingItem> items = repository.findByChargingId(chargingId);
        for (ChargingItem item : items) {
            item.delete();
            repository.save(item);
        }
    }

}
