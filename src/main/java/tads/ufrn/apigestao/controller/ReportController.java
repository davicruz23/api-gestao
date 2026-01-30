package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tads.ufrn.apigestao.service.ReportService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService service;

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @GetMapping("/{id}/chargings")
    public ResponseEntity<byte[]> gerarRelatorioCarregamento(@PathVariable Long id) {
        byte[] pdfBytes = service.generateChargingReport(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=carregamento_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @GetMapping("/products")
    public ResponseEntity<byte[]> gerarRelatorioProdutos() {
        byte[] pdfBytes = service.generateProductReport();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=produtos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @GetMapping("/clients-by-city")
    public ResponseEntity<byte[]> gerarRelatorioClientes(@RequestParam String city) {

        byte[] pdf = service.generateClientsByCityReport(city);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=clientes-" + city + ".pdf")
                .body(pdf);
    }

}
