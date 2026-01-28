package tads.ufrn.apigestao.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tads.ufrn.apigestao.controller.mapper.CollectionAttemptMapper;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.collector.CollectionAttemptDTO;
import tads.ufrn.apigestao.domain.dto.collector.CollectionAttemptMapsDTO;
import tads.ufrn.apigestao.enums.AttemptType;
import tads.ufrn.apigestao.enums.PaymentType;
import tads.ufrn.apigestao.exception.BusinessException;
import tads.ufrn.apigestao.exception.ResourceNotFoundException;
import tads.ufrn.apigestao.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CollectionAttemptService {

    private final CollectionAttemptRepository repository;
    private final InstallmentRepository installmentRepository;
    private final CollectorRepository collectorRepository;

    @Transactional
    public CollectionAttemptDTO recordAttempt(
            Long collectorId,
            Long installmentId,
            Double amount,
            PaymentType paymentMethod,
            Double latitude,
            Double longitude,
            String note,
            LocalDateTime newDueDate
    ) {
        Collector collector = collectorRepository.findById(collectorId)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrador não encontrado!"));

        Installment installment = installmentRepository.findById(installmentId)
                .orElseThrow(() -> new BusinessException("Parcela não encontrada!"));

        CollectionAttempt attempt = new CollectionAttempt();
        attempt.setCollector(collector);
        attempt.setInstallment(installment);
        attempt.setAttemptAt(LocalDateTime.now());
        attempt.setType(amount != null && amount > 0 ? AttemptType.PAYMENT : AttemptType.ATTEMPT);
        attempt.setAmount(amount);
        attempt.setPaymentMethod(paymentMethod);
        attempt.setLatitude(latitude);
        attempt.setLongitude(longitude);
        attempt.setNote(note);
        attempt.setNewDueDate(newDueDate);

        // Atualiza parcela se foi pagamento
        if (amount != null && amount > 0) {
            installment.setPaid(true);
            installment.setPaymentDate(LocalDateTime.now());

            if (newDueDate != null) {
                installment.setDueDate(LocalDate.from(newDueDate));
            }
            installmentRepository.save(installment);
        }

        CollectionAttempt savedAttempt = repository.save(attempt);

        return new CollectionAttemptDTO(savedAttempt);
    }

    @Transactional(readOnly = true)
    public List<CollectionAttemptMapsDTO> findPaidAttemptsByCollectorAndSale(Long collectorId, Long saleId) {
        List<CollectionAttempt> attempts = repository.findPaidAttemptsByCollectorAndSale(collectorId, saleId);

        return attempts.stream()
                .map(CollectionAttemptMapper::mapper)
                .toList();
    }

}
