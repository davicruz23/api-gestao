package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.controller.mapper.ChargingMapper;
import tads.ufrn.apigestao.domain.Charging;
import tads.ufrn.apigestao.domain.Product;
import tads.ufrn.apigestao.domain.dto.charging.ChargingDTO;
import tads.ufrn.apigestao.domain.dto.charging.UpsertChargingDTO;
import tads.ufrn.apigestao.repository.ChargingRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChangingService {

    private final ChargingRepository repository;
    private final ModelMapper mapper;

    public List<Charging> findAll() {
        return repository.findAll();
    }

    public Charging findById(Long id) {
        Optional<Charging> obj = repository.findById(id);
        return obj.orElseThrow(() -> new NotFoundException("ChargingMapper não encontrado!"));
    }

    public Charging store(UpsertChargingDTO charging) {
        return repository.save(mapper.map(charging, Charging.class));
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
