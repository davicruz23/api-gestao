package tads.ufrn.apigestao.controller;

import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tads.ufrn.apigestao.controller.mapper.CollectorMapper;
import tads.ufrn.apigestao.domain.CollectionAttempt;
import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.Installment;
import tads.ufrn.apigestao.domain.Sale;
import tads.ufrn.apigestao.domain.dto.LocationSaleDTO;
import tads.ufrn.apigestao.domain.dto.collector.*;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorIdUserDTO;
import tads.ufrn.apigestao.domain.dto.installment.InstallmentPaidDTO;
import tads.ufrn.apigestao.domain.dto.sale.SaleCollectorDTO;
import tads.ufrn.apigestao.enums.PaymentType;
import tads.ufrn.apigestao.service.ApprovalLocationService;
import tads.ufrn.apigestao.service.CollectionAttemptService;
import tads.ufrn.apigestao.service.CollectorService;
import tads.ufrn.apigestao.service.PixService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/collector")
public class CollectorController {

    private final CollectorService service;
    private final CollectionAttemptService collectionAttemptService;
    private final PixService pixService;
    private final ApprovalLocationService approvalLocationService;

    @PostMapping("/{collectorId}/assign/{city}")
    public ResponseEntity<CollectorSalesAssignedDTO> assignSalesByCity(
            @PathVariable Long collectorId,
            @PathVariable String city
    ) {
        System.out.println("entrei no metodo de cidade");
        return ResponseEntity.ok(service.assignSalesByCity(collectorId, city));
    }

    @GetMapping("/{collectorId}/salesssssss")
    public ResponseEntity<List<Sale>> getSales(@PathVariable Long collectorId) {
        return ResponseEntity.ok(service.getSales(collectorId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CollectorDTO>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(CollectorMapper::mapper).toList());
    }

//    @GetMapping("/{collectorId}/sales")
//    public ResponseEntity<List<CollectorSalesDTO>> getCollectorSales(@PathVariable Long collectorId) {
//        List<CollectorSalesDTO> salesDTO = service.getSalesByCollectorDTO(collectorId);
//        return ResponseEntity.ok(salesDTO);
//    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payInstallment(@PathVariable Long id) {
        try {
            InstallmentPaidDTO paidInstallment = service.markAsPaid(id);
            return ResponseEntity.ok(paidInstallment);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/commission")
    public ResponseEntity<CollectorCommissionDTO> getCommissionByPeriod(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "false") boolean saveHistory) {

        CollectorCommissionDTO dto = service.getCommissionByPeriod(id, startDate, endDate, saveHistory);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{collectorId}/sales")
    public ResponseEntity<Map<String, List<SaleCollectorDTO>>> getSalesByCollector(@PathVariable Long collectorId) {
        Map<String, List<SaleCollectorDTO>> salesByCity = service.findSalesByCollectorId(collectorId);
        return ResponseEntity.ok(salesByCity);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<CollectorIdUserDTO> getCollectorByUserId(@PathVariable Long userId) {
        CollectorIdUserDTO dto = service.getCollectorByUserId(userId);
        System.out.println("chamei o endpoint de seller user: "+ dto.toString());
        return ResponseEntity.ok(dto);
    }



    @PutMapping("/{collectorId}/installment/{installmentId}/collect")
    public ResponseEntity<CollectionAttemptDTO> collectInstallment(
            @PathVariable Long collectorId,
            @PathVariable Long installmentId,
            @RequestBody CollectionAttemptDTO dto
    ) {
        CollectionAttemptDTO attempt = collectionAttemptService.recordAttempt(
                collectorId,
                installmentId,
                dto.getAmount(),
                dto.getPaymentMethod(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getNote(),
                dto.getNewDueDate()
        );

        return ResponseEntity.ok(attempt);
    }

    @GetMapping("/installment/{id}/pix")
    public ResponseEntity<byte[]> getPixQrCode(@PathVariable Long id) {
        try {
            String brCode = pixService.generateBrCodeForInstallment(id);
            byte[] qrImage = pixService.generateQrCodeImage(brCode, 300, 300);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .cacheControl(CacheControl.noCache())
                    .header("Content-Disposition", "inline; filename=\"qrcode.png\"")
                    .body(qrImage);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/installment/{id}/pix/debug")
    public ResponseEntity<String> getPixCodeDebug(@PathVariable Long id) {
        try {
            String brCode = pixService.generateBrCodeForInstallment(id);
            return ResponseEntity.ok(brCode);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/collector/{collectorId}/sale/{saleId}/paid-attempts")
    public ResponseEntity<List<CollectionAttemptMapsDTO>> getPaidAttemptsByCollectorAndSale(
            @PathVariable Long collectorId,
            @PathVariable Long saleId
    ) {
        List<CollectionAttemptMapsDTO> list = collectionAttemptService.findPaidAttemptsByCollectorAndSale(collectorId, saleId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/collector/sale/{saleId}/location-sale")
    public ResponseEntity<List<LocationSaleDTO>> getLocationBySaleId(@PathVariable Long saleId) {
        List<LocationSaleDTO> list = approvalLocationService.getLocationSale(saleId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/check-location/{installmentId}")
    public ResponseEntity<Map<String, Object>> checkLocation(@PathVariable Long installmentId) {
        boolean withinRadius = service.isAttemptWithinApprovalLocation(installmentId);

        Map<String, Object> response = new HashMap<>();
        response.put("installmentId", installmentId);
        response.put("withinRadius", withinRadius);

        return ResponseEntity.ok(response);
    }

}
