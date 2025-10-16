package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentDTO;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentStatusDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductDTO;
import tads.ufrn.apigestao.domain.dto.product.ProductSaleDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleCollectorDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleLocationDTO;

import java.time.LocalDate;
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
                                                .id(inst.getId())
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

    public static SaleCollectorDTO saleCollector(Sale src) {
        String mapsUrl = null;

        if (src.getApprovalLocation() != null
                && src.getApprovalLocation().getLatitude() != null
                && src.getApprovalLocation().getLongitude() != null) {
            mapsUrl = String.format(
                    "https://www.google.com/maps/search/?api=1&query=%s,%s",
                    src.getApprovalLocation().getLatitude(),
                    src.getApprovalLocation().getLongitude()
            );
        }

        return SaleCollectorDTO.builder()
                .id(src.getId())
                .saleDate(src.getSaleDate())
                .client(src.getPreSale().getClient() != null
                        ? ClientMapper.clientSale(src.getPreSale().getClient())
                        : null)
                .products(
                        src.getPreSale().getItems().stream()
                                .map(item -> ProductMapper.mapperProductSale(item.getProduct()))
                                .toList()
                )
                .installments(
                        src.getInstallments() != 0 && src.getId() != null
                                ? src.getInstallmentsEntities().stream()
                                .map(InstallmentMapper::mapperStatus)
                                .toList()
                                : List.of()
                )
                .latitude(src.getApprovalLocation().getLatitude())
                .longitude(src.getApprovalLocation().getLongitude())
                .build();
    }


    public static SaleLocationDTO mapperSaleLocation(Sale src) {
        return SaleLocationDTO.builder()
                .saleId(src.getId())
                .idClient(src.getPreSale().getClient().getId())
                .clientName(src.getPreSale().getClient().getName())
                .build();
    }
}
