package com.example.booking.service.impl;

import com.example.booking.entity.Hotel;
import com.example.booking.entity.User;
import com.example.booking.exception.IncorrectMarkException;
import com.example.booking.repository.HotelRepository;
import com.example.booking.repository.HotelSpecification;
import com.example.booking.service.AbstractEntityService;
import com.example.booking.service.HotelService;
import com.example.booking.web.model.request.HotelsFilterRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class HotelServiceImpl extends AbstractEntityService<Hotel, UUID, HotelRepository>
        implements HotelService {

    public HotelServiceImpl(HotelRepository repository) {
        super(repository);
    }

    @Override
    public Hotel updateRating(UUID hotelId, int newMark) {

        Hotel hotel = repository.findById(hotelId).orElse(null);

        if (newMark < 1 || newMark > 5) {
            throw new IncorrectMarkException("You entered incorrect mark! Please enter mark 1-5!");
        }

        if (hotel != null) {

            int numberOfRating = hotel.getNumberOFMarks();
            double rating = hotel.getRating();
            double totalRating = rating * numberOfRating;

            totalRating = totalRating - rating + newMark;
            rating = numberOfRating == 0 ? totalRating : totalRating / numberOfRating;

            rating = Math.round(rating * 10) / 10.0;
            numberOfRating++;

            hotel.setRating(rating);
            hotel.setNumberOFMarks(numberOfRating);
        }
        return hotel;
    }

    @Override
    public Page<Hotel> filterBy(HotelsFilterRequest filter) {
        return repository.findAll(HotelSpecification.withFilter(filter),
                filter.getPagination().pageRequest());
    }

    @Override
    protected Hotel updateFields(Hotel oldEntity, Hotel newEntity) {
        if (!Objects.equals(oldEntity.getHotelName(), newEntity.getHotelName())) {
            oldEntity.setHotelName(newEntity.getHotelName());
        }
        if (!Objects.equals(oldEntity.getTitle(), newEntity.getTitle())) {
            oldEntity.setTitle(newEntity.getTitle());
        }
        if (!Objects.equals(oldEntity.getCity(), newEntity.getCity())) {
            oldEntity.setCity(newEntity.getCity());
        }
        if (!Objects.equals(oldEntity.getAddress(), newEntity.getAddress())) {
            oldEntity.setAddress(newEntity.getAddress());
        }
        if (!Objects.equals(oldEntity.getCenterDistance(), newEntity.getCenterDistance())) {
            oldEntity.setCenterDistance(newEntity.getCenterDistance());
        }
        log.info("UpdateFields in hotel: " + oldEntity.getHotelName());

        return oldEntity;
    }
}
