package com.park.ParkPro.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ParkResponseDto {
    private UUID id;
    private String name;
    private String location;
    private String description;
    private BigDecimal totalBudget;
    private BigDecimal remainingBudget;
    private BigDecimal totalDonations;
    private BigDecimal totalRevenue;
    private BigDecimal totalExpenses;
}
