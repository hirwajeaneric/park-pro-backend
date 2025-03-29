package com.park.ParkPro.dto;

import com.park.ParkPro.model.DonationStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class DonationResponseDto {
    private UUID id;
    private BigDecimal amount;
    private String donorName;
    private String donorEmail;
    private String reason;
    private UUID parkId;
    private String parkName;
    private String paymentMethod;
    private String paymentReference;
    private DonationStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
