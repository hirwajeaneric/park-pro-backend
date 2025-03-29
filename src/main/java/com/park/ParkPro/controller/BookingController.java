package com.park.ParkPro.controller;

import com.park.ParkPro.dto.BookingDto;
import com.park.ParkPro.dto.BookingResponseDto;
import com.park.ParkPro.service.BookingService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Management", description = "Endpoints for managing park bookings")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new booking")
    public ResponseEntity<BookingResponseDto> createBooking(
            @Valid @RequestBody BookingDto bookingDto) {
        BookingResponseDto response = bookingService.createBooking(bookingDto);
        return ResponseEntity
                .created(URI.create("/api/bookings/" + response.getId()))
                .body(response);
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Confirm a booking", description = "Requires ADMIN role")
    public ResponseEntity<BookingResponseDto> confirmBooking(
            @PathVariable UUID id,
            @RequestParam String paymentReference) {
        return ResponseEntity.ok(bookingService.confirmBooking(id, paymentReference));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER', 'PARK_MANAGER', 'VISITOR')")
    @Operation(summary = "Get booking by ID")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/park/{parkId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER', 'PARK_MANAGER')")
    @Operation(summary = "Get all bookings for a park")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByPark(@PathVariable UUID parkId) {
        return ResponseEntity.ok(bookingService.getBookingsByPark(parkId));
    }

    @GetMapping("/visitor/{visitorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VISITOR')")
    @Operation(summary = "Get all bookings for a visitor")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByVisitor(@PathVariable UUID visitorId) {
        return ResponseEntity.ok(bookingService.getBookingsByVisitor(visitorId));
    }

    @GetMapping("/park/{parkId}/date")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER', 'PARK_MANAGER')")
    @Operation(summary = "Get bookings for a park on a specific date")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByParkAndDate(
            @PathVariable UUID parkId,
            @RequestParam LocalDate date) {
        return ResponseEntity.ok(bookingService.getBookingsByParkAndDate(parkId, date));
    }

    @GetMapping("/park/{parkId}/revenue")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER', 'PARK_MANAGER')")
    @Operation(summary = "Get total revenue for a park")
    public ResponseEntity<BigDecimal> getTotalRevenueByPark(@PathVariable UUID parkId) {
        return ResponseEntity.ok(bookingService.getTotalRevenueByPark(parkId));
    }

    @GetMapping("/park/{parkId}/daily-stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER')")
    @Operation(summary = "Get daily booking statistics for a park")
    public ResponseEntity<List<Object[]>> getDailyBookingStats(@PathVariable UUID parkId) {
        return ResponseEntity.ok(bookingService.getDailyBookingStats(parkId));
    }

    @GetMapping("/park/{parkId}/activity-stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER', 'PARK_MANAGER')")
    @Operation(summary = "Get activity statistics for a park")
    public ResponseEntity<List<Object[]>> getActivityStats(@PathVariable UUID parkId) {
        return ResponseEntity.ok(bookingService.getActivityStats(parkId));
    }
}
