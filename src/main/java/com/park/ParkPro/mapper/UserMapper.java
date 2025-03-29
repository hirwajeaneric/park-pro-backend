package com.park.ParkPro.mapper;

import com.park.ParkPro.dto.UserDto;
import com.park.ParkPro.model.User;
import org.mapstruct.Mapping;

public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
