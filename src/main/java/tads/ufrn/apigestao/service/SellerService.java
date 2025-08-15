package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.Seller;
import tads.ufrn.apigestao.domain.dto.seller.SellerDTO;
import tads.ufrn.apigestao.domain.dto.seller.UpsertSellerDTO;
import tads.ufrn.apigestao.repository.SellerRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository repository;
    private final ModelMapper mapper;

    public List<Seller> findAll() {
        return repository.findAll();
    }

    public Seller findById(Long id) {
        Optional<Seller> seller = repository.findById(id);
        return seller.orElseThrow(() -> new NotFoundException("Seller not found"));
    }

    public Seller store(UpsertSellerDTO seller) {
        return repository.save(mapper.map(seller, Seller.class));
    }
}
