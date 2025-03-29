package com.park.ParkPro.dto;

import com.park.ParkPro.model.BudgetStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class BudgetResponseDto {
    private UUID id;
    private UUID parkId;
    private String parkName;
    private Integer fiscalYear;
    private BigDecimal totalAmount;
    private BigDecimal balance;
    private BudgetStatus status;
    private UUID createdById;
    private String createdByName;
    private UUID approvedById;
    private String approvedByName;
    private Instant createdAt;
    private Instant updatedAt;
    private List<BudgetCategoryResponseDto> categories;
}

