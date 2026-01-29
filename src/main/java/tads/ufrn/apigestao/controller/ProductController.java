package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tads.ufrn.apigestao.controller.mapper.ProductMapper;
import tads.ufrn.apigestao.controller.mapper.UserMapper;
import tads.ufrn.apigestao.domain.Product;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.domain.dto.product.UpsertProductDTO;
import tads.ufrn.apigestao.domain.dto.product.UptadeProductDTO;
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

//    @PreAuthorize("hasAnyRole('SUPERADMIN','VENDEDOR','COBRADOR','FISCAL','FUNCIONARIO')")
//    @GetMapping("/all")
//    public ResponseEntity<List<ProductDTO>> findAll(){
//        return ResponseEntity.ok().body(service.findAll().stream().map(ProductMapper::mapper).toList());
//    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','VENDEDOR','COBRADOR','FISCAL','FUNCIONARIO')")
    @GetMapping("/all")
    public ResponseEntity<Page<ProductDTO>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.getAll(name, page, size));
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','VENDEDOR','COBRADOR','FISCAL','FUNCIONARIO')")
    @GetMapping("/index")
    public ResponseEntity<Page<ProductDTO>> index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductDTO> result = service.index(page, size)
                .map(ProductMapper::mapper);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','VENDEDOR','COBRADOR','FISCAL','FUNCIONARIO')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(ProductMapper.mapper(service.findById(id)));
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @PostMapping
    public ResponseEntity<UpsertProductDTO> store(@RequestBody UpsertProductDTO model){
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(service.store(model).getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @DeleteMapping("{id}/delete")
    public ResponseEntity<ProductDTO> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UptadeProductDTO> updateProduct(@RequestBody UptadeProductDTO productDto) {
        Product updated = service.updateProduct(productDto);
        return ResponseEntity.ok(ProductMapper.mapperUptadeProduct(updated));
    }
}
