package com.example.booking.service.impl;

import com.example.booking.entity.Hotel;
import com.example.booking.entity.Room;
import com.example.booking.repository.RoomRepository;
import com.example.booking.service.AbstractEntityService;
import com.example.booking.service.HotelService;
import com.example.booking.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class RoomServiceImpl extends AbstractEntityService<Room, UUID, RoomRepository>
        implements RoomService {

    private final HotelService hotelService;

    public RoomServiceImpl(RoomRepository repository, HotelService hotelService) {
        super(repository);
        this.hotelService = hotelService;
    }

    @Override
    protected Room updateFields(Room oldEntity, Room newEntity) {
        if (!Objects.equals(oldEntity.getName(), newEntity.getName())) {
            oldEntity.setName(newEntity.getName());
        }
        if (!Objects.equals(oldEntity.getDescription(), newEntity.getDescription())) {
            oldEntity.setDescription(newEntity.getDescription());
        }
        if (!Objects.equals(oldEntity.getNumber(), newEntity.getNumber())) {
            oldEntity.setNumber(newEntity.getNumber());
        }
        if (!Objects.equals(oldEntity.getPrice(), newEntity.getPrice())) {
            oldEntity.setPrice(newEntity.getPrice());
        }
        if (!Objects.equals(oldEntity.getMaxCountGuests(), newEntity.getMaxCountGuests())) {
            oldEntity.setMaxCountGuests(newEntity.getMaxCountGuests());
        }
        if (!Objects.equals(oldEntity.getBusyFrom(), newEntity.getBusyFrom())) {
            oldEntity.setBusyFrom(newEntity.getBusyFrom());
        }
        if (!Objects.equals(oldEntity.getBusyTo(), newEntity.getBusyTo())) {
            oldEntity.setBusyTo(newEntity.getBusyTo());
        }
        log.info("UpdateFields in room: " + oldEntity.getName());
        return oldEntity;
    }

    @Override
    @Transactional
    public Room addRoom(Room room, UUID hotelId) {
        Hotel hotel = hotelService.findById(hotelId);

        hotel.addRoom(room);

        room.setName(room.getName() + " № " + room.getNumber());

        log.info("Add room to hotel: " + hotel.getHotelName());

        return save(room);
    }

    @Override
    @Transactional
    public Room updateRoom(Room room, UUID id, UUID hotelId) {
        if (hotelId != null) {
            Hotel hotel = hotelService.findById(hotelId);
            room.setHotel(hotel);
        }
        log.info("Update room: " + room.getName());

        return update(id, room);
    }
}
