package com.example.booking.web.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelsFilterRequest {

    private PaginationRequest pagination;

    private UUID id;

    private String hotelName;

    private String title;

    private String city;

    private String address;

    private Double centerDistance;

    private Double rating;

    private Integer numberOFMarks;

}
