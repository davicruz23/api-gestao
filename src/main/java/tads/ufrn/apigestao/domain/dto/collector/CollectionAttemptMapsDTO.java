package tads.ufrn.apigestao.domain.dto.collector;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.CollectionAttempt;
import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.Installment;
import tads.ufrn.apigestao.enums.AttemptType;
import tads.ufrn.apigestao.enums.PaymentType;

import java.time.LocalDateTime;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionAttemptMapsDTO {

    private Long idParcela;
    private String collectorName;
    @JsonIgnore
    private Double latitude;
    @JsonIgnore
    private Double longitude;
    private String collectionDate;
    private String note;
    private String mapsUrl;

    public String generateMapsUrl(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) return null;
        return this.mapsUrl = String.format(Locale.US,
                "https://www.google.com/maps/search/?api=1&query=%f,%f",
                latitude, longitude
        );
    }
}
