package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tads.ufrn.apigestao.controller.mapper.InspectorMapper;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorDTO;
import tads.ufrn.apigestao.domain.dto.inspector.UpsertInspectorDTO;
import tads.ufrn.apigestao.service.InspectorService;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/inspector")
public class InspectorController {

    private InspectorService service;

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
}
