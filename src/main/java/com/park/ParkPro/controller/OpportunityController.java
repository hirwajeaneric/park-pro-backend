package com.park.ParkPro.controller;

import com.park.ParkPro.dto.OpportunityApplicationDto;
import com.park.ParkPro.dto.OpportunityApplicationResponseDto;
import com.park.ParkPro.dto.OpportunityDto;
import com.park.ParkPro.dto.OpportunityResponseDto;
import com.park.ParkPro.model.ApplicationStatus;
import com.park.ParkPro.security.UserPrincipal;
import com.park.ParkPro.service.OpportunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/opportunities")
@RequiredArgsConstructor
@Tag(name = "Opportunity Management", description = "Endpoints for managing conservation opportunities")
@SecurityRequirement(name = "bearerAuth")
public class OpportunityController {

    private final OpportunityService opportunityService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PARK_MANAGER')")
    @Operation(summary = "Create a new opportunity")
    public ResponseEntity<OpportunityResponseDto> createOpportunity(
            @Valid @RequestBody OpportunityDto opportunityDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        OpportunityResponseDto response = opportunityService.createOpportunity(
                opportunityDto, userPrincipal.getId());
        return ResponseEntity
                .created(URI.create("/api/opportunities/" + response.getId()))
                .body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get opportunity by ID")
    public ResponseEntity<OpportunityResponseDto> getOpportunityById(@PathVariable UUID id) {
        return ResponseEntity.ok(opportunityService.getOpportunityById(id));
    }

    @GetMapping
    @Operation(summary = "Get all opportunities")
    public ResponseEntity<List<OpportunityResponseDto>> getAllOpportunities() {
        return ResponseEntity.ok(opportunityService.getAllOpportunities());
    }

    @GetMapping("/public")
    @Operation(summary = "Get open public opportunities")
    public ResponseEntity<List<OpportunityResponseDto>> getOpenPublicOpportunities() {
        return ResponseEntity.ok(opportunityService.getOpenPublicOpportunities());
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARK_MANAGER')")
    @Operation(summary = "Close an opportunity")
    public ResponseEntity<OpportunityResponseDto> closeOpportunity(@PathVariable UUID id) {
        return ResponseEntity.ok(opportunityService.closeOpportunity(id));
    }

    @PostMapping("/{id}/apply")
    @Operation(summary = "Apply for an opportunity")
    public ResponseEntity<OpportunityApplicationResponseDto> applyForOpportunity(
            @PathVariable UUID id,
            @Valid @RequestBody OpportunityApplicationDto applicationDto) {
        OpportunityApplicationResponseDto response = opportunityService.applyForOpportunity(
                id, applicationDto);
        return ResponseEntity
                .created(URI.create("/api/opportunities/" + id + "/applications/" + response.getId()))
                .body(response);
    }

    @GetMapping("/{id}/applications")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARK_MANAGER')")
    @Operation(summary = "Get applications for an opportunity")
    public ResponseEntity<List<OpportunityApplicationResponseDto>> getApplicationsForOpportunity(
            @PathVariable UUID id) {
        return ResponseEntity.ok(opportunityService.getApplicationsForOpportunity(id));
    }

    @PutMapping("/applications/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARK_MANAGER')")
    @Operation(summary = "Update application status")
    public ResponseEntity<OpportunityApplicationResponseDto> updateApplicationStatus(
            @PathVariable UUID id,
            @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(opportunityService.updateApplicationStatus(id, status));
    }

    @GetMapping("/search")
    @Operation(summary = "Search opportunities")
    public ResponseEntity<List<OpportunityResponseDto>> searchOpportunities(@RequestParam String query) {
        return ResponseEntity.ok(opportunityService.searchOpportunities(query));
    }
}
