package com.park.ParkPro.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BudgetCategoryDto {
    @NotNull(message = "Category name is required")
    private String name;

    @NotNull(message = "Allocated amount is required")
    private BigDecimal allocatedAmount;
}
