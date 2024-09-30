package com.example.booking.mapper;

import com.example.booking.entity.Hotel;
import com.example.booking.web.model.request.UpsertHotelRequest;
import com.example.booking.web.model.request.UpsertHotelUpdateRequest;
import com.example.booking.web.model.response.HotelResponse;
import com.example.booking.web.model.response.HotelUpdateResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface HotelMapper {

    Hotel upsertRequestToUpdateHotel(UpsertHotelUpdateRequest updateRequest);

    HotelResponse hotelToResponse(Hotel hotel);

    HotelUpdateResponse hotelUpdateToResponse(Hotel hotel);


}
