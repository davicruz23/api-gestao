package tads.ufrn.apigestao.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.enums.PaymentType;

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
    private LocalDateTime saleDate;

    @OneToOne
    @JoinColumn(name = "pre_sale_id", nullable = false)
    private PreSale preSale;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentType paymentMethod;

    @Min(1)
    private int installments;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "collector_id")
    private Collector collector;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Installment> installmentsEntities = new ArrayList<>();
}
