package tads.ufrn.apigestao.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "collector_id")
    private Collector collector;
}
