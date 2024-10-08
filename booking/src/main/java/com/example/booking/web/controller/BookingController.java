package com.example.booking.web.controller;

import com.example.booking.entity.Booking;
import com.example.booking.mapper.BookingMapper;
import com.example.booking.service.BookingService;
import com.example.booking.web.model.request.PaginationRequest;
import com.example.booking.web.model.request.UpsertBookingRequest;
import com.example.booking.web.model.response.BookingResponse;
import com.example.booking.web.model.response.HotelResponse;
import com.example.booking.web.model.response.ModelListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    private final BookingMapper bookingMapper;

    @GetMapping
    public ResponseEntity<ModelListResponse<BookingResponse>> findAll(@Valid PaginationRequest request) {
        Page<Booking> bookings = bookingService.findAll(request.pageRequest());
        return ResponseEntity.ok(
                ModelListResponse.<BookingResponse>builder()
                        .totalCount(bookings.getTotalElements())
                        .data(bookings.stream().map(bookingMapper::bookingToResponse).toList())
                        .build()
        );
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<BookingResponse> findById(@PathVariable UUID id) {
//        return ResponseEntity.ok(bookingMapper.bookingToResponse(bookingService.findById(id)));
//    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid UpsertBookingRequest request,
                                                         @RequestParam UUID roomId,
                                                         @RequestParam UUID userId) {
        Booking newBooking = bookingService.addBooking(bookingMapper.upsertRequestToBooking(request),
                roomId, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingMapper.bookingToResponse(newBooking));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<BookingResponse> updateBooking(@RequestBody @Valid UpsertBookingRequest request,
//                                                         @PathVariable UUID id,
//                                                         @RequestParam(required = false) UUID roomId,
//                                                         @RequestParam(required = false) UUID userId) {
//        Booking updatedBooking = bookingService.updateBooking(bookingMapper.upsertRequestToBooking(request),
//                id, roomId, userId);
//
//        return ResponseEntity.ok(bookingMapper.bookingToResponse(updatedBooking));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
//        bookingService.deleteById(id);
//
//        return ResponseEntity.noContent().build();
//    }
}
