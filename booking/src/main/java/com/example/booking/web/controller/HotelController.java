package com.example.booking.web.controller;

import com.example.booking.entity.Hotel;
import com.example.booking.mapper.HotelMapper;
import com.example.booking.service.HotelService;
import com.example.booking.web.model.request.UpsertHotelUpdateRequest;
import com.example.booking.web.model.response.HotelResponse;
import com.example.booking.web.model.response.HotelUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    private final HotelMapper hotelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(hotelMapper.hotelToResponse(hotelService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(@RequestBody @Valid UpsertHotelUpdateRequest request) {
        Hotel newHotel = hotelService.save(hotelMapper.upsertRequestToUpdateHotel(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelMapper.hotelToResponse(newHotel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelUpdateResponse> updateHotel(@RequestBody @Valid UpsertHotelUpdateRequest request,
                                                           @PathVariable UUID id) {
        Hotel updatedHotel = hotelService.update(id, hotelMapper.upsertRequestToUpdateHotel(request));

        return ResponseEntity.ok(hotelMapper.hotelUpdateToResponse(updatedHotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        hotelService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
