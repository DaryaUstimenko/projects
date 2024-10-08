package com.example.booking.repository;

import com.example.booking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID>, JpaSpecificationExecutor<Room> {
}
