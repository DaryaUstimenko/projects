package com.example.booking.service.impl;

import com.example.booking.entity.Booking;
import com.example.booking.entity.Room;
import com.example.booking.entity.UnavailableDates;
import com.example.booking.entity.User;
import com.example.booking.exception.WrongDatePeriodException;
import com.example.booking.repository.BookingRepository;
import com.example.booking.service.AbstractEntityService;
import com.example.booking.service.BookingService;
import com.example.booking.service.RoomService;
import com.example.booking.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Period;
import java.util.*;

@Service
@Slf4j
public class BookingServiceImpl extends AbstractEntityService<Booking, UUID, BookingRepository>
        implements BookingService {

    private final RoomService roomService;
    private final UserService userService;

    public BookingServiceImpl(BookingRepository repository, RoomService roomService, UserService userService) {
        super(repository);
        this.roomService = roomService;
        this.userService = userService;
    }

    @Override
    protected Booking updateFields(Booking oldEntity, Booking newEntity) {
        if (!Objects.equals(oldEntity.getBusyFrom(), newEntity.getBusyFrom())) {
            oldEntity.setBusyFrom(newEntity.getBusyFrom());
        }
        if (!Objects.equals(oldEntity.getBusyTo(), newEntity.getBusyTo())) {
            oldEntity.setBusyTo(newEntity.getBusyTo());
        }

        log.info("UpdateFields in booking: " + oldEntity.getId());

        return oldEntity;
    }

    @Override
    @Transactional
    public Booking addBooking(Booking booking, UUID roomId, UUID userId) {
        User user = userService.findById(userId);
        Room room = roomService.findById(roomId);

        Period period = Period.between(booking.getBusyFrom(), booking.getBusyTo());
        if (period.getDays() < 0) {
            throw new WrongDatePeriodException("You have entered a start date later " +
                    "than the final booking date! Please changed it!");
        }

        boolean isDateAvailable = room.getUnavailableDates().stream().allMatch(d -> {
            if (booking.getBusyFrom().isBefore(d.getBusyTo()) &&
                    booking.getBusyTo().isAfter(d.getBusyFrom())) {
                return false;
            } else {
                return true;
            }
        });

        if (isDateAvailable) {

            UnavailableDates unavailableDate = new UnavailableDates();
            unavailableDate.setBusyFrom(booking.getBusyFrom());
            unavailableDate.setBusyTo(booking.getBusyTo());
            unavailableDate.setRoom(room);

            room.addUnavailableDates(unavailableDate);
            room.addBooking(booking);
            user.addBooking(booking);

            booking.setBookingInfo("You created booking for duration from: " + booking.getBusyFrom() +
                    " to: " + booking.getBusyTo() +
                    " at the hotel: " + room.getHotel().getHotelName().toUpperCase(Locale.ROOT));
        } else {
            throw new WrongDatePeriodException("Those dates are already booked!");
        }

        log.info("Add booking for room: " + room.getName() + ". Now his room busy for duration: " +
                period.getDays());

        log.info("Add booking for user: " + user.getUsername());

        return save(booking);
    }

    @Override
    @Transactional
    public Booking updateBooking(Booking booking, UUID id, UUID roomId, UUID userId) {
        if (roomId != null) {
            Room room = roomService.findById(roomId);
            booking.setRoom(room);
        }
        log.info("Update booking: " + booking.getId());

        return update(id, booking);
    }

}
