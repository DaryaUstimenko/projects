package com.example.booking.web.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomsFilterRequest {

    private PaginationRequest pagination;

    private UUID id;

    private String description;

    private Integer minPrice;

    private Integer maxPrice;

    private Integer maxCountGuests;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Enter the date in the format: 2024-12-29")
    private String busyFrom;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Enter the date in the format: 2024-12-29")
    private String busyTo;

    private UUID hotelId;

}
