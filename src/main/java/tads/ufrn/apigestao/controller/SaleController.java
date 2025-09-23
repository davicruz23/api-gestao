package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tads.ufrn.apigestao.controller.mapper.SaleMapper;
import tads.ufrn.apigestao.domain.dto.sale.SaleDTO;
import tads.ufrn.apigestao.domain.dto.sale.SalesByCityDTO;
import tads.ufrn.apigestao.domain.dto.sale.UpsertSaleDTO;
import tads.ufrn.apigestao.service.SaleService;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sale")
public class SaleController {

    private SaleService service;

    @GetMapping("/all")
    public ResponseEntity<List<SaleDTO>> findAll(){
        return ResponseEntity.ok().body(service.findAll().stream().map(SaleMapper::mapper).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(SaleMapper.mapper(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<UpsertSaleDTO> store(@RequestBody UpsertSaleDTO model){
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(service.store(model).getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<SaleDTO> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sales/by-city")
    public List<SalesByCityDTO> getSalesByCity() {
        return service.getSalesGroupedByCity();
    }
}
