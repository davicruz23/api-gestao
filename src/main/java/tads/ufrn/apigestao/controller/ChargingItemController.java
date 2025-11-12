package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tads.ufrn.apigestao.controller.mapper.ChargingItemMapper;
import tads.ufrn.apigestao.controller.mapper.ChargingMapper;
import tads.ufrn.apigestao.domain.ChargingItem;
import tads.ufrn.apigestao.domain.dto.charging.ChargingDTO;
import tads.ufrn.apigestao.domain.dto.chargingItem.ChargingItemDTO;
import tads.ufrn.apigestao.service.ChargingItemService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/charging-itens")
public class ChargingItemController {

    private final ChargingItemService service;

    @PreAuthorize("hasAnyRole('SUPERADMIN','VENDEDOR','FUNCIONARIO')")
    @GetMapping("/all")
    public ResponseEntity<List<ChargingItemDTO>> findAll(){
        return ResponseEntity.ok().body(service.findAll().stream().map(ChargingItemMapper::mapper).toList());
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','VENDEDOR','FUNCIONARIO')")
    @GetMapping("/{id}")
    public ResponseEntity<ChargingItemDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(ChargingItemMapper.mapper(service.findById(id)));
    }
}
