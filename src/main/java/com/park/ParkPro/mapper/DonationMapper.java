package com.park.ParkPro.mapper;

import com.park.ParkPro.dto.DonationDto;
import com.park.ParkPro.dto.DonationResponseDto;
import com.park.ParkPro.model.Donation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DonationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "paymentReference", ignore = true)
    @Mapping(target = "park", ignore = true)
    Donation toEntity(DonationDto donationDto);

    @Mapping(source = "park.id", target = "parkId")
    @Mapping(source = "park.name", target = "parkName")
    DonationResponseDto toDto(Donation donation);
}
