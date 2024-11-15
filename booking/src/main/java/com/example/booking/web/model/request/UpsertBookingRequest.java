package com.example.booking.web.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertBookingRequest {

    private UUID id;

    private LocalDate busyFrom;

    private LocalDate busyTo;
}
