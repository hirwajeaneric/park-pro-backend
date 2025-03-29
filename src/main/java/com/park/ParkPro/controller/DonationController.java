package com.park.ParkPro.controller;

import com.park.ParkPro.dto.DonationDto;
import com.park.ParkPro.dto.DonationResponseDto;
import com.park.ParkPro.service.DonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
@Tag(name = "Donation Management", description = "Endpoints for managing park donations")
@SecurityRequirement(name = "bearerAuth")
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    @Operation(summary = "Create a new donation")
    public ResponseEntity<DonationResponseDto> createDonation(
            @Valid @RequestBody DonationDto donationDto) {
        DonationResponseDto response = donationService.createDonation(donationDto);
        return ResponseEntity
                .created(URI.create("/api/donations/" + response.getId()))
                .body(response);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Mark donation as completed", description = "Requires ADMIN role")
    public ResponseEntity<DonationResponseDto> completeDonation(
            @PathVariable UUID id,
            @RequestParam String paymentReference) {
        return ResponseEntity.ok(donationService.completeDonation(id, paymentReference));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER', 'PARK_MANAGER')")
    @Operation(summary = "Get donation by ID")
    public ResponseEntity<DonationResponseDto> getDonationById(@PathVariable UUID id) {
        return ResponseEntity.ok(donationService.getDonationById(id));
    }

    @GetMapping("/park/{parkId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER', 'PARK_MANAGER')")
    @Operation(summary = "Get all donations for a park")
    public ResponseEntity<List<DonationResponseDto>> getDonationsByPark(@PathVariable UUID parkId) {
        return ResponseEntity.ok(donationService.getDonationsByPark(parkId));
    }

    @GetMapping("/park/{parkId}/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER', 'PARK_MANAGER')")
    @Operation(summary = "Get completed donations for a park")
    public ResponseEntity<List<DonationResponseDto>> getCompletedDonationsByPark(
            @PathVariable UUID parkId) {
        return ResponseEntity.ok(donationService.getCompletedDonationsByPark(parkId));
    }

    @GetMapping("/park/{parkId}/total")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER', 'PARK_MANAGER')")
    @Operation(summary = "Get total donations for a park")
    public ResponseEntity<BigDecimal> getTotalDonationsByPark(@PathVariable UUID parkId) {
        return ResponseEntity.ok(donationService.getTotalDonationsByPark(parkId));
    }

    @GetMapping("/park/{parkId}/trends")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER')")
    @Operation(summary = "Get monthly donation trends for a park")
    public ResponseEntity<List<Object[]>> getMonthlyDonationTrends(@PathVariable UUID parkId) {
        return ResponseEntity.ok(donationService.getMonthlyDonationTrends(parkId));
    }
}
