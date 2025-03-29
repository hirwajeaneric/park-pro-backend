package com.park.ParkPro.mapper;

import com.park.ParkPro.dto.BookingDto;
import com.park.ParkPro.dto.BookingResponseDto;
import com.park.ParkPro.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookingMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "paymentReference", ignore = true)
    @Mapping(target = "visitor", ignore = true)
    @Mapping(target = "park", ignore = true)
    Booking toEntity(BookingDto bookingDto);

    @Mapping(source = "visitor.id", target = "visitorId")
    @Mapping(source = "visitor.firstName", target = "visitorName")
    @Mapping(source = "park.id", target = "parkId")
    @Mapping(source = "park.name", target = "parkName")
    BookingResponseDto toDto(Booking booking);
}
