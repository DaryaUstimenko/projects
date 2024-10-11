package com.example.booking.service;

import com.example.booking.entity.Booking;

import java.util.UUID;

public interface BookingService extends EntityService<Booking, UUID> {

    Booking addBooking(Booking booking, UUID roomId, UUID userId);

    Booking updateBooking(Booking booking, UUID id, UUID roomId, UUID userId);

    void deleteBooking(UUID id);
}
