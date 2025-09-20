package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tads.ufrn.apigestao.controller.mapper.SellerMapper;
import tads.ufrn.apigestao.domain.dto.charging.UpsertChargingDTO;
import tads.ufrn.apigestao.domain.dto.seller.SellerDTO;
import tads.ufrn.apigestao.domain.dto.seller.SellerIdUserDTO;
import tads.ufrn.apigestao.domain.dto.seller.UpsertSellerDTO;
import tads.ufrn.apigestao.service.SellerService;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/seller")
public class SellerController {

    private final SellerService service;

    @GetMapping("/all")
    public ResponseEntity<List<SellerDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll().stream().map(SellerMapper::mapper).toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<SellerDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(SellerMapper.mapper(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<UpsertSellerDTO> store(@RequestBody UpsertSellerDTO model){
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(service.store(model).getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<SellerIdUserDTO> getSellerByUserId(@PathVariable Long userId) {
        SellerIdUserDTO dto = service.getSellerByUserId(userId);
        System.out.println("chamei o endpoint de seller user: "+ dto.toString());
        return ResponseEntity.ok(dto);
    }
}
