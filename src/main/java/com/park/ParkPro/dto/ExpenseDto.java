package com.park.ParkPro.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ExpenseDto {

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank
    private String description;

    @NotBlank
    private String category;

    @NotNull
    private UUID budgetCategoryId;

    @NotNull
    private UUID parkId;

    private String receiptUrl;
}
