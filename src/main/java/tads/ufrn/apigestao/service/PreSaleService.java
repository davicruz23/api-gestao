package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.controller.mapper.InspectorMapper;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorHistoryPreSaleDTO;
import tads.ufrn.apigestao.domain.dto.preSale.UpsertPreSaleDTO;
import tads.ufrn.apigestao.domain.dto.preSaleItem.UpsertPreSaleItemDTO;
import tads.ufrn.apigestao.domain.dto.seller.SellerCommissionDTO;
import tads.ufrn.apigestao.enums.PreSaleStatus;
import tads.ufrn.apigestao.repository.CommissionHistoryRepository;
import tads.ufrn.apigestao.repository.PreSaleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreSaleService {

    private final PreSaleRepository repository;
    private final ClientService clientService;
    private final SellerService sellerService;
    private final ChangingService changingService;
    private final InspectorService inspectorService;
    private final CommissionHistoryRepository commissionHistoryRepository;

    public List<PreSale> findAll(){
        return repository.findAll();
    }

    public PreSale findById(Long id) {
        Optional<PreSale> preSale = repository.findById(id);
        return preSale.orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional
    public PreSale store(UpsertPreSaleDTO dto) {
        System.out.println("=== INICIANDO MÉTODO STORE ===");
        System.out.println("DTO recebido: " + dto.toString());
        System.out.println("UserID do DTO: " + dto.getSellerId());

        // Busca ou cria o cliente
        System.out.println("=== BUSCANDO/CRIANDO CLIENTE ===");
        Client client = clientService.store(dto.getClient());
        System.out.println("Cliente criado/encontrado: " + client.getId() + " - " + client.getName());

        // Busca o vendedor - agora usando o ID do DTO
        System.out.println("=== BUSCANDO VENDEDOR ===");
        System.out.println("Buscando seller com ID: " + dto.getSellerId());
        Seller seller = sellerService.findById(dto.getSellerId());
        System.out.println("Seller encontrado: " + (seller != null ? seller.getId() + " - " + seller.getUser().getName() : "NULL"));

        if (seller == null) {
            System.out.println("=== ERRO: VENDEDOR NÃO ENCONTRADO ===");
            throw new RuntimeException("Vendedor não encontrado: " + dto.getSellerId());
        }

        Inspector inspector = inspectorService.findById(1L);

        PreSale preSale = new PreSale();
        preSale.setPreSaleDate(dto.getPreSaleDate());
        preSale.setSeller(seller);
        preSale.setClient(client);
        preSale.setInspector(inspector);
        preSale.setStatus(PreSaleStatus.PENDENTE);
        preSale.setItems(new ArrayList<>());
        System.out.println("Pré-venda criada com seller: " + seller.getId() + " e client: " + client.getId());

        Charging charging = changingService.findById(dto.getChargingId());

        for (UpsertPreSaleItemDTO prodDTO : dto.getProducts()) {
            assert charging != null;
            ChargingItem ci = charging.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(prodDTO.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        System.out.println("ERRO: Produto não encontrado no carregamento: " + prodDTO.getProductId());
                        return new RuntimeException("Produto não encontrado no carregamento: " + prodDTO.getProductId());
                    });

            if (ci.getQuantity() < prodDTO.getQuantity()) {
                System.out.println("ERRO: Quantidade insuficiente. Disponível: " + ci.getQuantity() + ", Solicitado: " + prodDTO.getQuantity());
                throw new RuntimeException("Quantidade insuficiente no carregamento para o produto: " + ci.getProduct().getName());
            }

            ci.setQuantity(ci.getQuantity() - prodDTO.getQuantity());

            PreSaleItem preSaleItem = new PreSaleItem();
            preSaleItem.setPreSale(preSale);
            preSaleItem.setProduct(ci.getProduct());
            preSaleItem.setChargingItem(ci);
            preSaleItem.setQuantity(prodDTO.getQuantity());
            preSale.getItems().add(preSaleItem);
        }

        double totalPreSale = preSale.getItems().stream()
                .mapToDouble(item -> item.getProduct().getValue() * item.getQuantity())
                .sum();
        preSale.setTotalPreSale(totalPreSale);

        return repository.save(preSale);
    }

    public void deleteById(Long id){
        PreSale client = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product not found"));
        repository.save(client);

    }

    public PreSale approvePreSale(Long preSaleId, Inspector inspector) {
        PreSale preSale = findById(preSaleId);
        preSale.setStatus(PreSaleStatus.APROVADA);
        preSale.setInspector(inspector);
        return repository.save(preSale);
    }

    @Transactional
    public PreSale rejectPreSale(Long preSaleId) {
        PreSale preSale = findById(preSaleId);

        preSale.setStatus(PreSaleStatus.RECUSADA);
        //preSale.setInspector(inspector);

        // Para cada item da pré-venda
        for (PreSaleItem item : preSale.getItems()) {
            int quantity = item.getQuantity();

            ChargingItem chargingItem = item.getChargingItem();
            if (chargingItem != null) {
                // devolve a quantidade reservada
                chargingItem.setQuantity(chargingItem.getQuantity() + quantity);
            }
            // não mexe no Product.amount global
        }

        repository.save(preSale);

        return preSale;
    }

    public List<PreSale> listAllPreSales(Long inspectorId, PreSaleStatus status) {
        return repository.findByInspectorIdAndStatus(inspectorId, status);
    }

    public List<PreSale> getPreSalesBySeller(Long sellerId) {
        return repository.findBySellerId(sellerId);
    }

    @Transactional
    public SellerCommissionDTO getCommissionByPeriod(Long sellerId, LocalDate startDate, LocalDate endDate, boolean saveHistory) {

        LocalDateTime now = LocalDateTime.now();

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("A data final não pode ser anterior à data inicial.");
        }

        Seller seller = sellerService.findById(sellerId);

        List<PreSale> preSales = getPreSalesBySeller(sellerId).stream()
                .filter(preSale -> preSale.getStatus() == PreSaleStatus.APROVADA)
                .filter(preSale -> preSale.getPreSaleDate() != null
                        && !preSale.getPreSaleDate().isBefore(startDate)
                        && !preSale.getPreSaleDate().isAfter(endDate))
                .toList();

        double totalCommission = preSales.stream()
                .mapToDouble(preSale -> {
                    double total = preSale.getTotalPreSale() != null ? preSale.getTotalPreSale() : 0.0;
                    double rate = total <= 1000 ? 0.09 : 0.045;
                    return total * rate;
                })
                .sum();

        if (saveHistory) {
            CommissionHistory history = new CommissionHistory();
            history.setSeller(seller);
            history.setGeneratedAt(now);
            history.setStartDate(startDate);
            history.setEndDate(endDate);
            history.setAmount(totalCommission);
            commissionHistoryRepository.save(history);
        }

        return new SellerCommissionDTO(
                seller.getId(),
                seller.getUser().getName(),
                startDate,
                endDate,
                totalCommission
        );
    }

    @Transactional
    public List<InspectorHistoryPreSaleDTO> findPreSalesByInspector(Long inspectorId) {
        List<PreSale> preSales = repository.findAllByInspectorId(inspectorId);
        return preSales.stream()
                .map(InspectorMapper::mapperHistory)
                .toList();
    }

}
