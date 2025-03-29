package com.park.ParkPro.dto;

import com.park.ParkPro.model.ExpenseStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ExpenseResponseDto {
    private UUID id;
    private BigDecimal amount;
    private String description;
    private String category;
    private UUID budgetCategoryId;
    private String budgetCategoryName;
    private UUID parkId;
    private String parkName;
    private UUID createdById;
    private String createdByName;
    private ExpenseStatus status;
    private String receiptUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
