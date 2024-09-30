package com.example.booking.web.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertRoomRequest {

    private UUID id;

    @NotNull
    @Size(min = 3, max = 20, message = "Min size for name: {min}. Max size is: {max}")
    private String name;

    @NotNull
    @Size(min = 3, max = 120, message = "Min size for name: {min}. Max size is: {max}")
    private String description;

    @NotNull
    private int number;

    @NotNull
    private int price;

    @NotNull
    private int maxCountGuests;
}
