package com.park.ParkPro.mapper;

import com.park.ParkPro.dto.OpportunityDto;
import com.park.ParkPro.dto.OpportunityResponseDto;
import com.park.ParkPro.model.Opportunity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OpportunityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "applications", ignore = true)
    Opportunity toEntity(OpportunityDto opportunityDto);

    @Mapping(source = "createdBy.id", target = "createdBy")
    @Mapping(source = "createdBy.firstName", target = "creatorName")
    @Mapping(source = "applications.size()", target = "applicationCount")
    OpportunityResponseDto toDto(Opportunity opportunity);
}
