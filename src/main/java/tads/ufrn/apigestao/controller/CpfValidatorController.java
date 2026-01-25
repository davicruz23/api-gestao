package tads.ufrn.apigestao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tads.ufrn.apigestao.service.CpfValidatorService;

@RestController
@RequestMapping("/api/cpf")
@RequiredArgsConstructor
public class CpfValidatorController {

    private final CpfValidatorService service;

    @GetMapping("/validar/{cpf}")
    public ResponseEntity<Boolean> validarCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.isValid(cpf));
    }
}
