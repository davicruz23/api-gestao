package tads.ufrn.apigestao.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tads.ufrn.apigestao.controller.mapper.InspectorMapper;
import tads.ufrn.apigestao.controller.mapper.PreSaleMapper;
import tads.ufrn.apigestao.controller.mapper.SaleMapper;
import tads.ufrn.apigestao.domain.Inspector;
import tads.ufrn.apigestao.domain.PreSale;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorDTO;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorHistoryPreSaleDTO;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorIdUserDTO;
import tads.ufrn.apigestao.domain.dto.inspector.UpsertInspectorDTO;
import tads.ufrn.apigestao.domain.dto.preSale.PreSaleDTO;
import tads.ufrn.apigestao.domain.dto.sale.ApprovePreSaleDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleDTO;
import tads.ufrn.apigestao.domain.dto.seller.SellerIdUserDTO;
import tads.ufrn.apigestao.enums.PreSaleStatus;
import tads.ufrn.apigestao.service.InspectorService;
import tads.ufrn.apigestao.service.PreSaleService;
import tads.ufrn.apigestao.service.SaleService;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/inspector")
public class InspectorController {

    private final InspectorService service;
    private final PreSaleService preSaleService;
    private final SaleService saleService;

    @GetMapping("/all")
    public ResponseEntity<List<InspectorDTO>> findAll(){
        return ResponseEntity.ok().body(service.findAll().stream().map(InspectorMapper::mapper).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspectorDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(InspectorMapper.mapper(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<UpsertInspectorDTO> store(@RequestBody UpsertInspectorDTO model){
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(service.store(model).getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<InspectorDTO> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{inspectorId}/pre-sales/pending")
    public ResponseEntity<List<PreSaleDTO>> listPending(@PathVariable Long inspectorId) {
        return ResponseEntity.ok(preSaleService.listAllPreSales(inspectorId, PreSaleStatus.PENDENTE).stream().map(PreSaleMapper::mapper).toList());
    }

    @PostMapping("/pre-sales/{preSaleId}/approve")
    public ResponseEntity<SaleDTO> approve(
            @PathVariable Long preSaleId,
            @RequestBody @Valid ApprovePreSaleDTO dto) {

        Inspector inspector = service.findById(dto.getInspectorId());

        Sale sale = saleService.approvePreSale(
                preSaleId,
                inspector,
                dto.getPaymentMethod(),
                dto.getInstallments(),
                dto.getCashPaid()
        );

        return ResponseEntity.ok(SaleMapper.toDTO(sale));
    }

    @PostMapping("/pre-sales/{preSaleId}/reject")
    public ResponseEntity<PreSaleDTO> reject(@PathVariable Long preSaleId) {
        PreSale preSale = preSaleService.rejectPreSale(preSaleId);
        return ResponseEntity.ok(PreSaleMapper.mapper(preSale));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<InspectorIdUserDTO> getInspectorByUserId(@PathVariable Long userId) {
        InspectorIdUserDTO dto = service.getInspectorByUserId(userId);
        System.out.println("chamei o endpoint de seller user: "+ dto.toString());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{inspectorId}/pre-sales-history")
    public ResponseEntity<List<InspectorHistoryPreSaleDTO>> getPreSalesByInspector(@PathVariable Long inspectorId) {
        List<InspectorHistoryPreSaleDTO> list = preSaleService.findPreSalesByInspector(inspectorId);
        return ResponseEntity.ok(list);
    }

}
