package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tads.ufrn.apigestao.service.AddressService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService service;

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getDistinctCities() {
        return ResponseEntity.ok(service.getDistinctCities());
    }
}
