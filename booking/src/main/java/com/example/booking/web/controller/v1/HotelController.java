package com.example.booking.web.controller.v1;

import com.example.booking.entity.Hotel;
import com.example.booking.mapper.HotelMapper;
import com.example.booking.service.HotelService;
import com.example.booking.web.model.request.HotelsFilterRequest;
import com.example.booking.web.model.request.PaginationRequest;
import com.example.booking.web.model.request.UpsertHotelUpdateRequest;
import com.example.booking.web.model.response.HotelResponse;
import com.example.booking.web.model.response.HotelUpdateResponse;
import com.example.booking.web.model.response.ModelListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    private final HotelMapper hotelMapper;

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ModelListResponse<HotelResponse>> filterBy(@Valid HotelsFilterRequest filter,
                                                                     @Valid PaginationRequest request) {
        filter.setPagination(request);
        Page<Hotel> hotels = hotelService.filterBy(filter);

        return ResponseEntity.ok(
                ModelListResponse.<HotelResponse>builder()
                        .totalCount(hotels.getTotalElements())
                        .data(hotels.stream().map(hotelMapper::hotelToResponse).toList())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(hotelMapper.hotelToResponse(hotelService.findById(id)));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> createHotel(@RequestBody @Valid UpsertHotelUpdateRequest request) {
        Hotel newHotel = hotelService.save(hotelMapper.upsertRequestToUpdateHotel(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelMapper.hotelToResponse(newHotel));
    }

    @PutMapping("/rating")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> updateHotelRating(@RequestParam UUID hotelId, @RequestParam int mark) {

        Hotel hotelWithNewRating = hotelService.updateRating(hotelId, mark);
        Hotel updatedHotel = hotelService.update(hotelId, hotelWithNewRating);

        return ResponseEntity.ok(hotelMapper.hotelToResponse(updatedHotel));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<HotelUpdateResponse> updateHotel(
            @PathVariable UUID id,
            @RequestBody @Valid UpsertHotelUpdateRequest request) {

        Hotel updatedHotel = hotelService.update(id, hotelMapper.upsertRequestToUpdateHotel(request));

        return ResponseEntity.ok(hotelMapper.hotelUpdateToResponse(updatedHotel));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        hotelService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
