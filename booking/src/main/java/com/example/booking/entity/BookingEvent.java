package com.example.booking.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "booking")
public class BookingEvent {

    @Id
    private ObjectId id;

    private UUID userId;

    private LocalDate checkIn;

    private LocalDate checkOut;
}
