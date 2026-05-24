package tads.ufrn.apigestao.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.enums.CommissionReason;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CommissionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "collector_id", nullable = true)
    private Collector collector;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = true)
    private Seller seller;

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal totalCommission;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal paymentPercentage;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal previousPaidAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommissionReason reason;
}