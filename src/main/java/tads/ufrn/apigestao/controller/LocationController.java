package tads.ufrn.apigestao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tads.ufrn.apigestao.domain.UserLocation;
import tads.ufrn.apigestao.domain.dto.location.LocationRequestDTO;
import tads.ufrn.apigestao.service.LocationService;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService service;

    @PostMapping
    public ResponseEntity<Void> saveLocation(
            @RequestBody LocationRequestDTO dto
    ) {
        service.saveLocation(dto);
        return ResponseEntity.noContent().build();
    }

    //@PreAuthorize("hasAnyRole('SUPERADMIN')")
    @GetMapping("/{userId}/latest")
    public ResponseEntity<UserLocation> getLatestLocation(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                service.getLatestLocation(userId)
        );
    }
}
