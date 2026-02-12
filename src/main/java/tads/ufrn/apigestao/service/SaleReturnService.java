package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.returnSale.ReturnSaleDTO;
import tads.ufrn.apigestao.domain.dto.returnSale.ReturnSaleItemDTO;
import tads.ufrn.apigestao.domain.dto.returnSale.ReturnSaleRequest;
import tads.ufrn.apigestao.domain.dto.returnSale.SaleReturnDTO;
import tads.ufrn.apigestao.enums.SaleStatus;
import tads.ufrn.apigestao.exception.BusinessException;
import tads.ufrn.apigestao.repository.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleReturnService {

    private final SaleService saleService;
    private final SaleReturnRepository saleReturnRepository;
    private final InstallmentRepository installmentRepository;
    private final SaleRepository saleRepository;
    private final ProductService productService;

    @Transactional
    public List<SaleReturnDTO> returnSale(Long saleId, ReturnSaleRequest request) {

        Sale sale = saleService.findById(saleId);

        SaleStatus newStatus = SaleStatus.fromValue(request.getStatus());

        if (saleReturnRepository.existsBySaleId(saleId)
                && (newStatus == SaleStatus.DEVOLVIDO_CLIENTE || newStatus == SaleStatus.DESISTENCIA)) {
            throw new BusinessException("Essa venda já foi devolvida.");
        }

        OffsetDateTime now = OffsetDateTime.now();

        sale.setStatus(newStatus);
        saleRepository.save(sale);

        switch (newStatus) {

            case DESISTENCIA -> {

                int totalProducts = sale.getPreSale().getItems().stream()
                        .mapToInt(PreSaleItem::getQuantity)
                        .sum();

                List<Installment> futureInstallments =
                        installmentRepository.findAllBySaleIdAndPaidFalseOrderByDueDateDesc(saleId);

                if (futureInstallments.isEmpty()) {
                    break;
                }

                if (totalProducts <= 1) {

                    for (Installment inst : futureInstallments) {
                        inst.setAmount(BigDecimal.ZERO);
                        inst.setPaidAmount(BigDecimal.ZERO);
                        inst.setPaid(true);

                    }

                    installmentRepository.saveAll(futureInstallments);
                    break;
                }

                BigDecimal discount = BigDecimal.ZERO;

                Map<Long, PreSaleItem> saleItemsMap =
                        sale.getPreSale().getItems().stream()
                                .collect(Collectors.toMap(
                                        i -> i.getProduct().getId(),
                                        Function.identity()
                                ));

                for (ReturnSaleItemDTO dto : request.getItems()) {

                    PreSaleItem item = saleItemsMap.get(dto.getProductId());

                    if (item == null) {
                        throw new BusinessException(
                                "Produto " + dto.getProductId() + " não pertence à venda."
                        );
                    }

                    if (dto.getQuantityReturned() <= 0) {
                        throw new BusinessException("Quantidade devolvida inválida.");
                    }

                    if (dto.getQuantityReturned() > item.getQuantity()) {
                        throw new BusinessException(
                                "Quantidade devolvida maior do que a comprada para o produto "
                                        + item.getProduct().getName()
                        );
                    }

                    BigDecimal unitPrice = item.getProduct().getValue();
                    BigDecimal itemDiscount =
                            unitPrice.multiply(BigDecimal.valueOf(dto.getQuantityReturned()));

                    discount = discount.add(itemDiscount);
                }

                // aplica abatimento parcela por parcela
                for (Installment inst : futureInstallments) {

                    if (discount.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }

                    BigDecimal instAmount = inst.getAmount();

                    if (instAmount.compareTo(discount) <= 0) {
                        inst.setAmount(BigDecimal.ZERO);
                        inst.setPaidAmount(BigDecimal.ZERO);

                        inst.setPaid(true);


                        discount = discount.subtract(instAmount);

                    } else {
                        inst.setAmount(instAmount.subtract(discount));
                        inst.setPaidAmount(BigDecimal.ZERO);
                        discount = BigDecimal.ZERO;
                    }
                }

                installmentRepository.saveAll(futureInstallments);
            }

            case REAVIDO -> {

                List<Installment> futureInstallments =
                        installmentRepository.findAllBySaleIdAndPaidFalseOrderByDueDateAsc(saleId);

                for (Installment inst : futureInstallments) {
                    inst.setAmount(BigDecimal.ZERO);
                    inst.setPaidAmount(BigDecimal.ZERO);
                    inst.setPaid(true);
                }

                installmentRepository.saveAll(futureInstallments);

                // 2) cria SaleReturn para TODOS os itens da venda
                List<SaleReturn> saleReturns = new ArrayList<>();

                for (PreSaleItem item : sale.getPreSale().getItems()) {

                    SaleReturn saleReturn = SaleReturn.builder()
                            .sale(sale)
                            .returnDate(now)
                            .productId(item.getProduct().getId())
                            .quantityReturned(item.getQuantity()) // recolhe tudo
                            .saleStatus(newStatus)
                            .build();

                    saleReturns.add(saleReturn);
                }

                saleReturnRepository.saveAll(saleReturns);

                // evita criar duplicado no final do método
                return saleReturns.stream()
                        .map(sr -> new SaleReturnDTO(
                                sr.getId(),
                                sr.getSale().getId(),
                                sr.getProductId(),
                                sr.getReturnDate(),
                                sr.getSaleStatus()
                        ))
                        .toList();
            }

            case ATIVO, DEFEITO_PRODUTO -> {
                // sem ação extra
            }

            default -> throw new BusinessException("Status inválido para devolução: " + newStatus);
        }

        List<SaleReturn> saleReturns = new ArrayList<>();

        for (ReturnSaleItemDTO dto : request.getItems()) {

            SaleReturn saleReturn = SaleReturn.builder()
                    .sale(sale)
                    .returnDate(now)
                    .productId(dto.getProductId())
                    .quantityReturned(dto.getQuantityReturned())
                    .saleStatus(newStatus)
                    .build();

            saleReturns.add(saleReturn);
        }

        saleReturnRepository.saveAll(saleReturns);

        // 4) retorna DTO
        return saleReturns.stream()
                .map(sr -> new SaleReturnDTO(
                        sr.getId(),
                        sr.getSale().getId(),
                        sr.getProductId(),
                        sr.getReturnDate(),
                        sr.getSaleStatus()
                ))
                .toList();
    }

}

