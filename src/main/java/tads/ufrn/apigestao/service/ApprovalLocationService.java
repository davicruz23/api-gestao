package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tads.ufrn.apigestao.controller.mapper.CollectionAttemptMapper;
import tads.ufrn.apigestao.controller.mapper.LocationSaleMapper;
import tads.ufrn.apigestao.domain.ApprovalLocation;
import tads.ufrn.apigestao.domain.CollectionAttempt;
import tads.ufrn.apigestao.domain.dto.LocationSaleDTO;
import tads.ufrn.apigestao.domain.dto.collector.CollectionAttemptMapsDTO;
import tads.ufrn.apigestao.repository.ApprovalLocationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalLocationService {

    private final ApprovalLocationRepository repository;

    @Transactional(readOnly = true)
    public List<LocationSaleDTO> getLocationSale(Long saleId) {
        List<ApprovalLocation> attempts = repository.locationBySaleId(saleId);

        return attempts.stream()
                .map(LocationSaleMapper::mapper)
                .toList();
    }
}
