package tads.ufrn.apigestao.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.enums.PaymentType;
import tads.ufrn.apigestao.enums.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numberSale;
    private LocalDate saleDate;

    @OneToOne
    @JoinColumn(name = "pre_sale_id", nullable = false)
    private PreSale preSale;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentType paymentMethod;

    @Min(1)
    private Integer installments;
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "collector_id")
    private Collector collector;

    @OneToMany(mappedBy = "sale", fetch = FetchType.LAZY)
    private List<SaleReturn> saleReturns = new ArrayList<>();

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Installment> installmentsEntities = new ArrayList<>();

    @OneToOne(mappedBy = "sale", cascade = CascadeType.ALL)
    private ApprovalLocation approvalLocation;
}
