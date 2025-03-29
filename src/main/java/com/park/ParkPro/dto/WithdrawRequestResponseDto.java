package com.park.ParkPro.dto;

import com.park.ParkPro.model.WithdrawStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class WithdrawRequestResponseDto {
    private UUID id;
    private BigDecimal amount;
    private String reason;
    private String description;
    private UUID requesterId;
    private String requesterName;
    private UUID approverId;
    private String approverName;
    private UUID budgetCategoryId;
    private String budgetCategoryName;
    private UUID parkId;
    private String parkName;
    private WithdrawStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
