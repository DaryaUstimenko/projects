package com.example.booking.mapper;

import com.example.booking.entity.Room;
import com.example.booking.web.model.request.UpsertRoomRequest;
import com.example.booking.web.model.response.RoomResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoomMapper {

    Room upsertRequestToRoom(UpsertRoomRequest request);

    @Mapping(source = "hotel.hotelName", target = "hotelName")
    RoomResponse roomToResponse(Room room);
}
