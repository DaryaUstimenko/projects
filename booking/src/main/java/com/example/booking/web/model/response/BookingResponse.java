package com.example.booking.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private UUID id;

    private LocalDate busyFrom;

    private LocalDate busyTo;

    private String bookingInfo;

    private String roomName;

    private String username;
}
