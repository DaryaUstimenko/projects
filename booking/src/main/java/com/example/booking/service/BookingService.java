package com.example.booking.service;

import com.example.booking.entity.Booking;
import com.example.booking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingService extends EntityService<Booking, UUID> {

    Booking addBooking(Booking booking, UUID roomId, UUID userId);

    Booking updateBooking(Booking booking, UUID id, UUID roomId);

    void deleteBooking(UUID id);

    Page<Booking> findAllByUser(User user, Pageable pageable);

    List<LocalDate> getAllUnavailableDates(Booking booking);
}
