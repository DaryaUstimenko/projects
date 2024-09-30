package com.example.booking.service.impl;

import com.example.booking.entity.Hotel;
import com.example.booking.repository.HotelRepository;
import com.example.booking.service.AbstractEntityService;
import com.example.booking.service.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class HotelServiceImpl extends AbstractEntityService<Hotel, UUID, HotelRepository>
        implements HotelService{

    public HotelServiceImpl(HotelRepository repository) {super(repository);}

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
