package com.example.booking.service;

import com.example.booking.entity.Room;
import com.example.booking.web.model.request.RoomsFilterRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;


public interface RoomService extends EntityService<Room, UUID> {
    Room addRoom(Room room, UUID hotelId);

    Room updateRoom(Room room, UUID id, UUID hotelId);

    Page<Room> filterBy(RoomsFilterRequest filter);
}
