package com.park.ParkPro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParkDto {

    @NotBlank(message = "Park name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    private String description;
}
