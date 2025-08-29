package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductSaleDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleDTO;

import java.util.List;
import java.util.stream.Collectors;

public class SaleMapper {
    public static SaleDTO mapper(Sale src){
        return SaleDTO.builder()
                .id(src.getId())
                .numberSale(src.getNumberSale())
                .saleDate(src.getSaleDate())
                .clientName(src.getPreSale().getClient().getName())
                .paymentType(src.getPaymentMethod().toString())
                .nParcel(src.getInstallments())
                .total(src.getTotal())
                .products(src.getPreSale().getItems().stream()
                        .map(item -> ProductMapper.mapperProductSale(item.getProduct()))
                        .toList())
                .installments(
                        src.getInstallments() != 0 && src.getId() != null ?
                                src.getInstallmentsEntities().stream()
                                        .map(inst -> InstallmentDTO.builder()
                                                .dueDate(inst.getDueDate())
                                                .amount(inst.getAmount())
                                                .paid(inst.isPaid())
                                                .build())
                                        .toList()
                                : List.of()
                )
                .build();

    }

    public static SaleDTO toDTO(Sale sale) {
        if (sale == null) return null;

        return SaleDTO.builder()
                .numberSale(sale.getNumberSale())
                .saleDate(sale.getSaleDate())
                .paymentType(sale.getPaymentMethod().name())
                .nParcel(sale.getInstallments())
                .clientName(sale.getPreSale().getClient().getName())
                .total(sale.getTotal())
                .build();
    }
}
