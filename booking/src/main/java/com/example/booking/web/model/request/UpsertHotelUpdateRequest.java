package com.example.booking.web.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertHotelUpdateRequest {

    private UUID id;

    @NotNull
    @Size(min = 3, max = 20, message = "Min size for name: {min}. Max size is: {max}")
    private String hotelName;

    @NotNull
    @Size(min = 3, max = 40, message = "Min size for title: {min}. Max size is: {max}")
    private String title;

    @NotNull
    @Size(min = 3, max = 20, message = "Min size for city: {min}. Max size is: {max}")
    private String city;

    @NotNull
    @Size(min = 10, max = 120, message = "Min size for address: {min}. Max size is: {max}")
    private String address;

    @NotNull
    @Min(value = (long) 0.0, message = "Not less then 0.0")
    private double centerDistance;
}
