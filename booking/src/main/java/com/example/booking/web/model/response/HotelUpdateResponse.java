package com.example.booking.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelUpdateResponse {

    private UUID id;

    private String hotelName;

    private String title;

    private String city;

    private String address;

    private String centerDistance;
}
