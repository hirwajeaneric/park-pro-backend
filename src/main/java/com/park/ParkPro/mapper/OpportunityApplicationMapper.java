package com.park.ParkPro.mapper;

import com.park.ParkPro.dto.OpportunityApplicationDto;
import com.park.ParkPro.dto.OpportunityApplicationResponseDto;
import com.park.ParkPro.model.OpportunityApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OpportunityApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "opportunity", ignore = true)
    OpportunityApplication toEntity(OpportunityApplicationDto applicationDto);

    @Mapping(source = "opportunity.id", target = "opportunityId")
    @Mapping(source = "opportunity.title", target = "opportunityTitle")
    OpportunityApplicationResponseDto toDto(OpportunityApplication application);
}
