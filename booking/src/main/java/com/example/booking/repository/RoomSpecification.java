package com.example.booking.repository;

import com.example.booking.entity.Room;
import com.example.booking.web.model.request.RoomsFilterRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public interface RoomSpecification {
    static Specification<Room> withFilter(RoomsFilterRequest filter) {

        return Specification.where(byRoomId(filter.getId()))
                .and(byDescription(filter.getDescription()))
                .and(byPriceRange(filter.getMinPrice(), filter.getMaxPrice()))
                .and(byMaxCountGuests(filter.getMaxCountGuests()))
                .and(byBusyDates(filter.getBusyFrom(), filter.getBusyTo()))
                .and(byHotelId(filter.getHotelId()));
    }

    static Specification<Room> byRoomId(UUID roomId) {
        return (root, query, cb) -> {
            if (roomId == null) {
                return null;
            }
            return cb.equal(root.get("room").get("id"), roomId);
        };
    }

    static Specification<Room> byDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.containsWhitespace(description)) {
                return null;
            }

            return criteriaBuilder.equal(root.get(Room.Fields.description), description);
        };
    }

    static Specification<Room> byPriceRange(Integer minPrice, Integer maxPrice) {
        return (root, query, cb) -> {
            if (maxPrice == null && minPrice == null) {
                return null;
            }
            if (minPrice == null) {
                return cb.lessThanOrEqualTo(root.get(Room.Fields.price), maxPrice);
            }

            if (maxPrice == null) {
                return cb.greaterThanOrEqualTo(root.get(Room.Fields.price), minPrice);
            }

            return cb.between(root.get(Room.Fields.price), minPrice, maxPrice);
        };
    }

    static Specification<Room> byMaxCountGuests(Integer maxCountGuests) {
        return (root, query, cb) -> {
            if (maxCountGuests == null) {
                return null;
            }

            return cb.lessThanOrEqualTo(root.get(Room.Fields.maxCountGuests), maxCountGuests);
        };
    }

    static Specification<Room> byBusyDates(String busyFrom, String busyTo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return (root, query, cb) -> {
            if (busyFrom == null || busyTo == null) {
                return null;
            }

            LocalDate busyFromDate = LocalDate.parse(busyFrom, formatter);
            LocalDate busyToDate = LocalDate.parse(busyTo, formatter);

            return cb.not(cb.and(cb.lessThanOrEqualTo(root.get(Room.Fields.unavailableDates).get("busyFrom"), busyToDate),
                    cb.greaterThanOrEqualTo(root.get(Room.Fields.unavailableDates).get("busyTo"), busyFromDate)));
        };

    }

    static Specification<Room> byHotelId(UUID hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return null;
            }
            return cb.equal(root.get("hotel").get("id"), hotelId);
        };
    }
}