package tads.ufrn.apigestao.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.enums.AttemptType;
import tads.ufrn.apigestao.enums.PaymentType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CollectionAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Parcela relacionada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "installment_id", nullable = false)
    private Installment installment;

    // Cobrador que marcou
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collector_id", nullable = false)
    private Collector collector;

    @Column(name = "attempt_at", nullable = false)
    private LocalDateTime attemptAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AttemptType type;

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentType paymentMethod;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "location_accuracy")
    private Double locationAccuracy;

    @Column(name = "within_range")
    private Boolean withinRange;

    @Column(name = "distance_meters")
    private Double distanceMeters;

    @Column(name = "new_due_date")
    private LocalDateTime newDueDate;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "server_recorded_at", nullable = false)
    private LocalDateTime serverRecordedAt = LocalDateTime.now();

    @Column(name = "receipt_ref")
    private String receiptRef;

}

