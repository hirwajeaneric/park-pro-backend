package com.park.ParkPro.dto;

import com.park.ParkPro.model.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class OpportunityApplicationResponseDto {
    private UUID id;
    private UUID opportunityId;
    private String opportunityTitle;
    private String firstName;
    private String lastName;
    private String email;
    private String applicationLetter;
    private ApplicationStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
