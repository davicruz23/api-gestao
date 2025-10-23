package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Installment;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentDTO;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentStatusDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InstallmentMapper {
    public static InstallmentDTO mapper(Installment src) {
        return InstallmentDTO.builder()
                .dueDate(src.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"))))
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
                .dueDate(src.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"))))
                .amount(src.getAmount())
                .paid(src.isPaid())
                .status(status)
                .build();
    }
}
