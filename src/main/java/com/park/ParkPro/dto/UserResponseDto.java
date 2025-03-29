package com.park.ParkPro.dto;

import com.park.ParkPro.model.Role;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class UserResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private UUID parkId;
    private String parkName;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}

