package tads.ufrn.apigestao.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.enums.PaymentType;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    private PaymentType paymentMethod;

    private int installments;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "collector_id")
    private Collector collector;
}
