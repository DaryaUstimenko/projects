package com.example.booking.repository;


import com.example.booking.entity.Hotel;
import com.example.booking.web.model.request.HotelsFilterRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.UUID;

public interface HotelSpecification {
    static Specification<Hotel> withFilter(HotelsFilterRequest filter) {
        return Specification.where(byHotelId(filter.getId()))
                .and(byHotelName(filter.getHotelName()))
                .and(byHotelTitle(filter.getTitle()))
                .and(byCity(filter.getCity()))
                .and(byHotelAddress(filter.getAddress()))
                .and(byCenterDistanceToHotel(filter.getCenterDistance()))
                .and(byHotelRating(filter.getRating()))
                .and(byNumberOFMarks(filter.getNumberOFMarks()));
    }

    static Specification<Hotel> byHotelId(UUID hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return null;
            }
            return cb.equal(root.get(Hotel.Fields.id), hotelId);
        };
    }

    static Specification<Hotel> byHotelName(String hotelName) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(hotelName)) {
                return null;
            }

            return criteriaBuilder.equal(root.get(Hotel.Fields.hotelName), hotelName);
        };
    }

    static Specification<Hotel> byHotelTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.containsWhitespace(title)) {
                return null;
            }

            return criteriaBuilder.equal(root.get(Hotel.Fields.title), title);
        };
    }

    static Specification<Hotel> byCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(city)) {
                return null;
            }

            return criteriaBuilder.equal(root.get(Hotel.Fields.city), city);
        };
    }

    static Specification<Hotel> byHotelAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(address)) {
                return null;
            }

            return criteriaBuilder.equal(root.get(Hotel.Fields.address), address);
        };
    }

    static Specification<Hotel> byCenterDistanceToHotel(Double centerDistance) {
        return (root, query, criteriaBuilder) -> {
            if (centerDistance == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get(Hotel.Fields.centerDistance), centerDistance);
        };
    }

    static Specification<Hotel> byHotelRating(Double rating) {
        return (root, query, criteriaBuilder) -> {
            if (rating == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Hotel.Fields.rating), rating);
        };
    }

    static Specification<Hotel> byNumberOFMarks(Integer numberOFMarks) {
        return (root, query, criteriaBuilder) -> {
            if (numberOFMarks == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get(Hotel.Fields.numberOFMarks), numberOFMarks);
        };
    }
}
