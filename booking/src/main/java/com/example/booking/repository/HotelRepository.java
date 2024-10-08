package com.example.booking.repository;

import com.example.booking.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, UUID>, JpaSpecificationExecutor<Hotel> {
}
