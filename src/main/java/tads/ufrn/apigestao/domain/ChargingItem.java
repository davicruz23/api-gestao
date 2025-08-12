package tads.ufrn.apigestao.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChargingItem extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "charging_id")
    private Charging charging;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "pre_sale_id")
    private PreSale preSale;
}

