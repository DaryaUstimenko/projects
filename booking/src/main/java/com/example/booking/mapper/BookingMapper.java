package com.example.booking.mapper;

import com.example.booking.entity.Booking;
import com.example.booking.web.model.request.UpsertBookingRequest;
import com.example.booking.web.model.response.BookingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BookingMapper {

    Booking upsertRequestToBooking(UpsertBookingRequest request);

    @Mapping(source = "room.name", target = "roomName")
    @Mapping(source = "user.username", target = "username")
    BookingResponse bookingToResponse(Booking booking);
}
