package tads.ufrn.apigestao.domain;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Charging extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "charging", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargingItem> items = new ArrayList<>();

    public void addItem(Product product, Integer quantity) {
        ChargingItem item = new ChargingItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setCharging(this);
        this.items.add(item);
    }
}
