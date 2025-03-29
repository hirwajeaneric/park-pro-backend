package com.park.ParkPro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpportunityDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String details;

    @NotBlank
    private String type;

    @NotNull
    private String visibility;
}
