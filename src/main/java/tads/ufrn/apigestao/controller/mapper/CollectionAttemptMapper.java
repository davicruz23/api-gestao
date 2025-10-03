package tads.ufrn.apigestao.controller.mapper;

import tads.ufrn.apigestao.domain.CollectionAttempt;
import tads.ufrn.apigestao.domain.dto.collector.CollectionAttemptMapsDTO;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CollectionAttemptMapper {
    public static CollectionAttemptMapsDTO mapper(CollectionAttempt src) {
        if (src == null) return null;

        Double latitude = src.getLatitude();
        Double longitude = src.getLongitude();

        CollectionAttemptMapsDTO dto = CollectionAttemptMapsDTO.builder()
                .idParcela(src.getId())
                .collectorName(src.getCollector().getUser().getName())
                .collectionDate(src.getAttemptAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", new Locale("pt", "BR"))))
                .note(src.getNote())
                .build();

        dto.setMapsUrl(dto.generateMapsUrl(latitude, longitude));

        return dto;
    }
}
