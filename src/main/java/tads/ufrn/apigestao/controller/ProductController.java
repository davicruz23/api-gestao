package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tads.ufrn.apigestao.controller.mapper.ProductMapper;
import tads.ufrn.apigestao.controller.mapper.UserMapper;
import tads.ufrn.apigestao.domain.Product;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.domain.dto.product.UpsertProductDTO;
import tads.ufrn.apigestao.domain.dto.user.UpsertUserDTO;
import tads.ufrn.apigestao.domain.dto.user.UserDTO;
import tads.ufrn.apigestao.service.ProductService;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private ProductService service;

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> findAll(){
        return ResponseEntity.ok().body(service.findAll().stream().map(ProductMapper::mapper).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(ProductMapper.mapper(service.findUserById(id)));
    }

    @PostMapping
    public ResponseEntity<UpsertProductDTO> store(@RequestBody UpsertProductDTO model){
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(service.store(model).getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
