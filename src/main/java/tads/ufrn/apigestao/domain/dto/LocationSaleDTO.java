package tads.ufrn.apigestao.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tads.ufrn.apigestao.domain.dto.sale.SaleLocationDTO;

import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationSaleDTO {

    private SaleLocationDTO sale;
    @JsonIgnore
    private Double latitude;
    @JsonIgnore
    private Double longitude;
    private String mapsUrl;

    public String generateMapsUrl(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) return null;
        return this.mapsUrl = String.format(Locale.US,
                "https://www.google.com/maps/search/?api=1&query=%f,%f",
                latitude, longitude
        );
    }
}
