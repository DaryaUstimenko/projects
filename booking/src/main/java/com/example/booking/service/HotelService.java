package com.example.booking.service;

import com.example.booking.entity.Hotel;
import com.example.booking.web.model.request.HotelsFilterRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface HotelService extends EntityService<Hotel, UUID> {

    Page<Hotel> filterBy(HotelsFilterRequest filter);
}
