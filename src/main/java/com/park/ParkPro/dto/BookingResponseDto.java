package com.park.ParkPro.dto;

import com.park.ParkPro.model.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class BookingResponseDto {
    private UUID id;
    private UUID visitorId;
    private String visitorName;
    private String activity;
    private BigDecimal amount;
    private UUID parkId;
    private String parkName;
    private LocalDate visitDate;
    private BookingStatus status;
    private String paymentReference;
    private Instant createdAt;
    private Instant updatedAt;
}