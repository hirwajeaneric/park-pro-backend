package com.park.ParkPro.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class BudgetCategoryResponseDto {
    private UUID id;
    private String name;
    private BigDecimal allocatedAmount;
    private BigDecimal usedAmount;
    private BigDecimal balance;
    private BigDecimal utilizationPercentage;
}
