package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Installment;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentDTO;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentStatusDTO;

import java.time.LocalDate;

public class InstallmentMapper {
    public static InstallmentDTO mapper(Installment src) {
        return InstallmentDTO.builder()
                .dueDate(src.getDueDate())
                .paid(src.isPaid())
                .build();
    }

    public static InstallmentDTO mapperStatus(Installment src) {
        InstallmentStatusDTO status;

        if (src.isPaid()) {
            status = InstallmentStatusDTO.PAGA;
        } else if (src.getDueDate().isBefore(LocalDate.now())) {
            status = InstallmentStatusDTO.ATRASADA;
        } else {
            status = InstallmentStatusDTO.EM_DIA;
        }

        return InstallmentDTO.builder()
                .id(src.getId())
                .dueDate(src.getDueDate())
                .amount(src.getAmount())
                .paid(src.isPaid())
                .status(status)
                .build();
    }
}
