package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.returnSale.ReturnSaleDTO;
import tads.ufrn.apigestao.domain.dto.returnSale.SaleReturnDTO;
import tads.ufrn.apigestao.enums.SaleStatus;
import tads.ufrn.apigestao.exception.BusinessException;
import tads.ufrn.apigestao.repository.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleReturnService {

    private final SaleService saleService;
    private final SaleReturnRepository saleReturnRepository;
    private final InstallmentRepository installmentRepository;
    private final SaleRepository saleRepository;
    private final ProductService productService;

    @Transactional
    public SaleReturnDTO returnSale(Long saleId, Long productId, Integer quantityReturned,int status) {

        Sale sale = saleService.findById(saleId);

        SaleStatus newStatus = SaleStatus.fromValue(status);

        if (saleReturnRepository.existsBySaleId(saleId)
                && (newStatus == SaleStatus.DEVOLVIDO_CLIENTE || newStatus == SaleStatus.DESISTENCIA)) {
            throw new BusinessException("Essa venda já foi devolvida.");
        }

        OffsetDateTime now = OffsetDateTime.now();

        // 1) atualiza status da venda
        sale.setStatus(newStatus);
        saleRepository.save(sale);

        // 2) regras por status
        switch (newStatus) {

            case DESISTENCIA -> {

                // 2.1) Quantidade total de produtos na venda (considerando quantity)
                int totalProducts = sale.getPreSale().getItems().stream()
                        .mapToInt(PreSaleItem::getQuantity)
                        .sum();

                // 2.2) Parcelas futuras (não pagas)
                List<Installment> futureInstallments =
                        installmentRepository.findAllBySaleIdAndPaidFalseOrderByDueDateAsc(saleId);

                if (futureInstallments.isEmpty()) {
                    break;
                }

                // 2.3) Se só tem 1 produto: zera todas as parcelas futuras
                if (totalProducts <= 1) {

                    for (Installment inst : futureInstallments) {
                        inst.setAmount(BigDecimal.ZERO);
                        inst.setPaidAmount(BigDecimal.ZERO);

                        // 🔥 some da lista de cobranças
                        inst.setPaid(true);

                        // 🔥 soft delete
                        //inst.setDeletedAt(now);
                    }

                    installmentRepository.saveAll(futureInstallments);
                    break;
                }

                // 2.4) Se tem mais de 1 produto: abate valor proporcional ao que foi devolvido
                int qtyReturned = (quantityReturned == null ? 1 : quantityReturned);

                if (qtyReturned <= 0) {
                    throw new BusinessException("Quantidade devolvida inválida.");
                }

                PreSaleItem item = sale.getPreSale().getItems().stream()
                        .filter(i -> i.getProduct().getId().equals(productId))
                        .findFirst()
                        .orElseThrow(() -> new BusinessException("Produto não encontrado na venda."));

                if (qtyReturned > item.getQuantity()) {
                    throw new BusinessException("Quantidade devolvida maior do que a quantidade comprada.");
                }

                BigDecimal unitPrice = item.getProduct().getValue(); // ajuste para seu campo real
                BigDecimal discount = unitPrice.multiply(BigDecimal.valueOf(qtyReturned));

                // aplica abatimento parcela por parcela
                for (Installment inst : futureInstallments) {

                    if (discount.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }

                    BigDecimal instAmount = inst.getAmount();

                    if (instAmount.compareTo(discount) <= 0) {
                        // parcela zera
                        inst.setAmount(BigDecimal.ZERO);
                        inst.setPaidAmount(BigDecimal.ZERO);

                        // 🔥 some da lista de cobranças
                        inst.setPaid(true);

                        // 🔥 soft delete
                        inst.setDeletedAt(now);

                        discount = discount.subtract(instAmount);

                    } else {
                        // parcela reduz
                        inst.setAmount(instAmount.subtract(discount));
                        inst.setPaidAmount(BigDecimal.ZERO);

                        // aqui NÃO precisa dar paid=true nem soft delete
                        // pq ainda tem valor a cobrar nessa parcela
                        discount = BigDecimal.ZERO;
                    }
                }

                installmentRepository.saveAll(futureInstallments);
            }



            case ATIVO, DEFEITO_PRODUTO, DEVOLVIDO_CLIENTE -> {
                // sem ação extra
            }

            default -> throw new BusinessException("Status inválido para devolução: " + newStatus);
        }

        // 3) salva histórico
        SaleReturn saleReturn = SaleReturn.builder()
                .sale(sale)
                .returnDate(now)
                .productId(productId)
                .quantityReturned(quantityReturned)
                .saleStatus(newStatus)
                .build();

        SaleReturn saved = saleReturnRepository.save(saleReturn);

        // 4) retorna DTO
        return new SaleReturnDTO(
                saved.getId(),
                saved.getSale().getId(),
                saved.getProductId(),
                saved.getReturnDate(),
                saved.getSaleStatus()
        );
    }

}

