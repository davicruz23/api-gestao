package tads.ufrn.apigestao.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PreVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataPreVenda;
    private String statusVenda;
    @OneToMany
    private List<Mercadoria> mercadorias;
    @ManyToOne
    private Usuario usuario;
    @OneToOne
    private Cliente cliente;
    @ManyToOne
    private Fiscal fiscal;
}
