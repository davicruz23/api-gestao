package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.Installment;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentDTO;

public class InstallmentMapper {
    public static InstallmentDTO mapper(Installment src) {
        return InstallmentDTO.builder()
                .dueDate(src.getDueDate())
                .paid(src.isPaid())
                .build();
    }
}
