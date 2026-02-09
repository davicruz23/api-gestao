package tads.ufrn.apigestao.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import tads.ufrn.apigestao.domain.dto.returnSale.ReturnSaleRequest;
import tads.ufrn.apigestao.domain.dto.returnSale.SaleReturnDTO;
import tads.ufrn.apigestao.service.SaleReturnService;

import java.util.List;

@RestController
@RequestMapping("/sale-return")
@RequiredArgsConstructor
public class SaleReturnController {

    private final SaleReturnService service;

    @PostMapping("/{saleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<SaleReturnDTO> returnSale(
            @PathVariable Long saleId,
            @Valid @RequestBody ReturnSaleRequest request
    ) {
        return service.returnSale(saleId, request);
    }
}
