package tads.ufrn.apigestao.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    private LocalDate dueDate;
    private BigDecimal amount;
    private boolean paid = false;
    private LocalDateTime paymentDate;
    @Column(nullable = false)
    private boolean commissionable = true;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private BigDecimal paidAmount;

    @Transient
    private Boolean isValid;
}
