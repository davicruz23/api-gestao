package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.preSale.UpsertPreSaleDTO;
import tads.ufrn.apigestao.domain.dto.preSaleItem.UpsertPreSaleItemDTO;
import tads.ufrn.apigestao.enums.PreSaleStatus;
import tads.ufrn.apigestao.repository.PreSaleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreSaleService {

    private final PreSaleRepository repository;
    private final ClientService clientService;
    private final SellerService sellerService;
    private final ModelMapper mapper;
    private final ChangingService changingService;
    private final InspectorService inspectorService;

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

        System.out.println("=== CRIANDO PRÉ-VENDA ===");
        PreSale preSale = new PreSale();
        preSale.setPreSaleDate(dto.getPreSaleDate());
        preSale.setSeller(seller);
        preSale.setClient(client);
        preSale.setInspector(inspector);
        preSale.setStatus(PreSaleStatus.PENDENTE);
        preSale.setItems(new ArrayList<>());
        System.out.println("Pré-venda criada com seller: " + seller.getId() + " e client: " + client.getId());

        System.out.println("=== BUSCANDO CARREGAMENTO ===");
        System.out.println("Buscando charging com ID: " + dto.getChargingId());
        Charging charging = changingService.findById(dto.getChargingId());
        System.out.println("Charging encontrado: " + (charging != null ? charging.getId() + " - Itens: " + charging.getItems().size() : "NULL"));

        System.out.println("=== PROCESSANDO PRODUTOS ===");
        System.out.println("Quantidade de produtos no DTO: " + dto.getProducts().size());

        for (UpsertPreSaleItemDTO prodDTO : dto.getProducts()) {
            System.out.println("Processando produto ID: " + prodDTO.getProductId() + ", Quantidade: " + prodDTO.getQuantity());

            assert charging != null;
            ChargingItem ci = charging.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(prodDTO.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        System.out.println("ERRO: Produto não encontrado no carregamento: " + prodDTO.getProductId());
                        return new RuntimeException("Produto não encontrado no carregamento: " + prodDTO.getProductId());
                    });

            System.out.println("ChargingItem encontrado - Produto: " + ci.getProduct().getName() + ", Quantidade disponível: " + ci.getQuantity());

            if (ci.getQuantity() < prodDTO.getQuantity()) {
                System.out.println("ERRO: Quantidade insuficiente. Disponível: " + ci.getQuantity() + ", Solicitado: " + prodDTO.getQuantity());
                throw new RuntimeException("Quantidade insuficiente no carregamento para o produto: " + ci.getProduct().getName());
            }

            // Atualiza a quantidade no carregamento
            ci.setQuantity(ci.getQuantity() - prodDTO.getQuantity());
            System.out.println("Quantidade atualizada: " + ci.getQuantity() + " para produto: " + ci.getProduct().getName());

            PreSaleItem preSaleItem = new PreSaleItem();
            preSaleItem.setPreSale(preSale);
            preSaleItem.setProduct(ci.getProduct());
            preSaleItem.setChargingItem(ci);
            preSaleItem.setQuantity(prodDTO.getQuantity());

            preSale.getItems().add(preSaleItem);
            System.out.println("Item adicionado à pré-venda");
        }

        System.out.println("=== SALVANDO PRÉ-VENDA ===");
        PreSale savedPreSale = repository.save(preSale);
        System.out.println("Pré-venda salva com ID: " + savedPreSale.getId());
        System.out.println("=== MÉTODO FINALIZADO COM SUCESSO ===");

        return savedPreSale;
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

}
