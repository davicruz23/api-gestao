package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tads.ufrn.apigestao.controller.mapper.ChargingMapper;
import tads.ufrn.apigestao.domain.dto.charging.ChargingDTO;
import tads.ufrn.apigestao.domain.dto.charging.UpsertChargingDTO;
import tads.ufrn.apigestao.service.ChargingService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/charging")
@AllArgsConstructor
public class ChargingController {

    private final ChargingService service;

    @PreAuthorize("hasAnyRole('SUPERADMIN','VENDEDOR','FUNCIONARIO')")
    @GetMapping("/all")
    public ResponseEntity<List<ChargingDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','VENDEDOR','FUNCIONARIO')")
    @GetMapping("/current")
    public ResponseEntity<List<ChargingDTO>> findCurrent() {
        return ResponseEntity.ok(service.findCurrent());
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','VENDEDOR','FUNCIONARIO')")
    @GetMapping("/{id}")
    public ResponseEntity<ChargingDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(ChargingMapper.mapper(service.findEntityById(id)));
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','VENDEDOR','FUNCIONARIO')")
    @PostMapping
    public ResponseEntity<UpsertChargingDTO> store(@RequestBody UpsertChargingDTO model){
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(service.store(model).getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @DeleteMapping("{id}/delete")
    public ResponseEntity<ChargingDTO> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
