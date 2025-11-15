package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.seller.*;
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

    public void createFromUser(User user) {
        Seller seller = new Seller();
        seller.setUser(user);
        repository.save(seller);
    }

//    public void addCommission(Seller seller, double commissionValue) {
//        seller.setTotalCommission(seller.getTotalCommission() + commissionValue);
//        repository.save(seller);
//    }

    public SellerIdUserDTO getSellerByUserId(Long userId) {
        Seller seller = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Seller não encontrado para o usuário: " + userId));

        return new SellerIdUserDTO(seller.getId());
    }
}
