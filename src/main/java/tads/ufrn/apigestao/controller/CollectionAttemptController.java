//package tads.ufrn.apigestao.controller;
//
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import tads.ufrn.apigestao.domain.*;
//import tads.ufrn.apigestao.enums.PaymentType;
//import tads.ufrn.apigestao.service.CollectionAttemptService;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/collector")
//public class CollectionAttemptController {
//
//    private final CollectionAttemptService service;
//
//    @PutMapping("/{collectorId}/installment/{installmentId}/collect")
//    public ResponseEntity<CollectionAttempt> collectInstallment(
//            @PathVariable Long collectorId,
//            @PathVariable Long installmentId,
//            @RequestBody Map<String, Object> payload
//    ) {
//        Double amount = payload.get("amount") != null ? ((Number) payload.get("amount")).doubleValue() : null;
//        PaymentType paymentMethod = payload.get("paymentMethod") != null ?
//                PaymentType.valueOf((String) payload.get("paymentMethod")) : null;
//        Double latitude = payload.get("latitude") != null ? ((Number) payload.get("latitude")).doubleValue() : null;
//        Double longitude = payload.get("longitude") != null ? ((Number) payload.get("longitude")).doubleValue() : null;
//        String note = payload.get("note") != null ? (String) payload.get("note") : null;
//        LocalDateTime newDueDate = payload.get("newDueDate") != null ?
//                LocalDateTime.parse((String) payload.get("newDueDate")) : null;
//
//        CollectionAttempt attempt = service.recordAttempt(
//                collectorId, installmentId, amount, paymentMethod, latitude, longitude, note, newDueDate
//        );
//
//        return ResponseEntity.ok(attempt);
//    }
//
//    /*@GetMapping("/{collectorId}/attempts")
//    public ResponseEntity<List<CollectionAttempt>> getAttemptsByCollector(@PathVariable Long collectorId) {
//        List<CollectionAttempt> attempts = service.getAttemptsByCollector(collectorId);
//        return ResponseEntity.ok(attempts);
//    }*/
//}
