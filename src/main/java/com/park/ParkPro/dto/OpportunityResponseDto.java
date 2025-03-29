package com.park.ParkPro.dto;

import com.park.ParkPro.model.OpportunityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityResponseDto {
    private UUID id;
    private String title;
    private String description;
    private String details;
    private String type;
    private OpportunityStatus status;
    private String visibility;
    private UUID createdBy;
    private String creatorName;
    private Instant createdAt;
    private Instant updatedAt;
    private int applicationCount;
}
