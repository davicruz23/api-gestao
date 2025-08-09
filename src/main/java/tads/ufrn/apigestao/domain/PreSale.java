package tads.ufrn.apigestao.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PreSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime preSaleDate;

    @ManyToOne @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "preSale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargingItem> items = new ArrayList<>();
}



