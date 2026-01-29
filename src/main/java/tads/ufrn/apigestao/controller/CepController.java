package tads.ufrn.apigestao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tads.ufrn.apigestao.domain.dto.cep.CepResponse;
import tads.ufrn.apigestao.service.CepService;

@RestController
@RequestMapping("/api/cep")
@RequiredArgsConstructor
public class CepController {

    private final CepService service;

    @GetMapping("/{cep}")
    public CepResponse buscarCpf ( @PathVariable String cep ) {
        return service.buscarPorCep(cep);
    }
}
