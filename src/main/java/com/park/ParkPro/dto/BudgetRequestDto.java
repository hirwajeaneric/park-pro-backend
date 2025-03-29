package com.park.ParkPro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class BudgetRequestDto {

    @NotNull(message = "Fiscal year is required")
    @Min(value = 2000, message = "Fiscal year must be valid")
    private Integer fiscalYear;

    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    private List<BudgetCategoryDto> categories;
}
