package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tads.ufrn.apigestao.controller.mapper.ClientMapper;
import tads.ufrn.apigestao.controller.mapper.ProductMapper;
import tads.ufrn.apigestao.domain.dto.client.ClientDTO;
import tads.ufrn.apigestao.domain.dto.client.UpsertClientDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.domain.dto.product.UpsertProductDTO;
import tads.ufrn.apigestao.service.ClientService;
import tads.ufrn.apigestao.service.ProductService;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/client")
public class ClientController {

    private ClientService service;

    @PreAuthorize("hasAnyRole('SUPERADMIN','COBRADOR','FISCAL','VENDEDOR')")
    @GetMapping("/all")
    public ResponseEntity<List<ClientDTO>> findAll(){
        return ResponseEntity.ok().body(service.findAll().stream().map(ClientMapper::mapper).toList());
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','COBRADOR','FISCAL','VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(ClientMapper.mapper(service.findById(id)));
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','COBRADOR','FISCAL','VENDEDOR')")
    @PostMapping
    public ResponseEntity<UpsertClientDTO> store(@RequestBody UpsertClientDTO model){
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(service.store(model).getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @DeleteMapping("{id}/delete")
    public ResponseEntity<ClientDTO> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
