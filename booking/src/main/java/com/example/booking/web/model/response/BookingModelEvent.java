package com.example.booking.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingModelEvent {

    private UUID userId;

    private LocalDate checkIn;

    private LocalDate checkOut;
}
