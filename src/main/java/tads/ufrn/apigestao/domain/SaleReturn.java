package tads.ufrn.apigestao.domain;

import jakarta.persistence.*;
import lombok.*;
import tads.ufrn.apigestao.enums.ReturnReason;
import tads.ufrn.apigestao.enums.SaleStatus;

import java.time.OffsetDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SaleReturn extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @Column(nullable = false)
    private OffsetDateTime returnDate;

    private Long productId;
    private Integer quantityReturned;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleStatus saleStatus;

}
