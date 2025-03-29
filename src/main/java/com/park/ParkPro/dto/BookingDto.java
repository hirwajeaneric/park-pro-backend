package com.park.ParkPro.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class BookingDto {

    @NotNull
    private UUID visitorId;

    @NotBlank
    private String activity;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull
    private UUID parkId;

    @NotNull
    @FutureOrPresent(message = "Visit date must be in the present or future")
    private LocalDate visitDate;
}