package com.example.booking.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {

    private UUID id;

    private String name;

    private String description;

    private String hotelName;

    private int number;

    private int price;

    private int maxCountGuests;

}
