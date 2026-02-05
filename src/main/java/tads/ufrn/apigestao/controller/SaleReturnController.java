package tads.ufrn.apigestao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import tads.ufrn.apigestao.domain.dto.returnSale.SaleReturnDTO;
import tads.ufrn.apigestao.service.SaleReturnService;

@RestController
@RequestMapping("/sale-return")
@RequiredArgsConstructor
public class SaleReturnController {

    private final SaleReturnService service;

    @PostMapping("/{saleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SaleReturnDTO returnSale(@PathVariable Long saleId, Long productId, Integer quantityReturned,int status) {
        return service.returnSale(saleId, productId, quantityReturned, status);
    }
}
