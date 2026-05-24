package tads.ufrn.apigestao.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tads.ufrn.apigestao.domain.dto.cep.CoordinatesDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public CoordinatesDTO getCoordinates(String street, String number, String city, String state, String zipCode) {
        CoordinatesDTO coordinates = searchCoordinatesByZipCode(zipCode);

        if (coordinates != null) {
            return coordinates;
        }

        coordinates = searchCoordinatesByStreetAndCity(street, city, state);

        if (coordinates != null) {
            return coordinates;
        }
        return null;
    }

    private CoordinatesDTO searchCoordinatesByZipCode(String zipCode) {
        try {
            zipCode = cleanZipCode(zipCode);

            if (zipCode.isBlank()) {
                return null;
            }

            String url = "https://cep.awesomeapi.com.br/json/" + zipCode;

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> result = response.getBody();

            if (result == null || result.isEmpty()) {
                return null;
            }

            Object latObj = result.get("lat");
            Object lngObj = result.get("lng");

            if (latObj == null || lngObj == null) {
                return null;
            }

            if (latObj.toString().isBlank() || lngObj.toString().isBlank()) {
                return null;
            }

            BigDecimal latitude = new BigDecimal(latObj.toString());
            BigDecimal longitude = new BigDecimal(lngObj.toString());

            return new CoordinatesDTO(latitude, longitude);

        } catch (Exception e) {
            return null;
        }
    }

    private CoordinatesDTO searchCoordinatesByStreetAndCity(String street, String city, String state) {
        street = clean(street);
        city = clean(city);
        state = clean(state);

        if (street.isBlank() || city.isBlank() || state.isBlank()) {
            return null;
        }

        String streetOnly = removeNeighborhoodFromStreet(street);

        String fullAddress = String.format(
                "%s, %s, %s, Brasil",
                streetOnly,
                city,
                state
        );

        return searchCoordinatesOnNominatim(fullAddress);
    }

    private CoordinatesDTO searchCoordinatesOnNominatim(String fullAddress) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://nominatim.openstreetmap.org/search")
                    .queryParam("q", fullAddress)
                    .queryParam("format", "json")
                    .queryParam("limit", "1")
                    .queryParam("countrycodes", "br")
                    .queryParam("addressdetails", "1")
                    .build()
                    .encode()
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "apigestao/1.0 (davifieledeus@gmail.com)");
            headers.set("Accept-Language", "pt-BR,pt;q=0.9");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            List<Map<String, Object>> results = response.getBody();

            if (results == null || results.isEmpty()) {
                return null;
            }

            Map<String, Object> location = results.get(0);

            Object latObj = location.get("lat");
            Object lonObj = location.get("lon");

            if (latObj == null || lonObj == null) {
                return null;
            }

            if (latObj.toString().isBlank() || lonObj.toString().isBlank()) {
                return null;
            }

            BigDecimal latitude = new BigDecimal(latObj.toString());
            BigDecimal longitude = new BigDecimal(lonObj.toString());

            return new CoordinatesDTO(latitude, longitude);

        } catch (Exception e) {
            return null;
        }
    }

    private String removeNeighborhoodFromStreet(String street) {
        if (street == null) {
            return "";
        }

        street = street.trim();

        if (street.contains(" - ")) {
            String[] parts = street.split(" - ", 2);
            return clean(parts[0]);
        }

        return street;
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }

    private String cleanZipCode(String zipCode) {
        if (zipCode == null) {
            return "";
        }

        return zipCode.replaceAll("\\D", "").trim();
    }
}