package com.example.booking.web.controller.v1;

import com.example.booking.aop.AuthorizeAction;
import com.example.booking.entity.Booking;
import com.example.booking.mapper.BookingMapper;
import com.example.booking.service.BookingService;
import com.example.booking.service.KafkaEventService;
import com.example.booking.web.model.request.PaginationRequest;
import com.example.booking.web.model.request.UpsertBookingRequest;
import com.example.booking.web.model.response.BookingResponse;
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
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    private final BookingMapper bookingMapper;

    private final KafkaEventService kafkaEventService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ModelListResponse<BookingResponse>> findAll(@Valid PaginationRequest request) {
        Page<Booking> bookings = bookingService.findAll(request.pageRequest());
        return ResponseEntity.ok(
                ModelListResponse.<BookingResponse>builder()
                        .totalCount(bookings.getTotalElements())
                        .data(bookings.stream().map(bookingMapper::bookingToResponse).toList())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @AuthorizeAction(actionType = "findById")
    public ResponseEntity<BookingResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingMapper.bookingToResponse(bookingService.findById(id)));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid UpsertBookingRequest request,
                                                         @RequestParam UUID roomId,
                                                         @RequestParam UUID userId) {
        Booking newBooking = bookingService.addBooking(bookingMapper.upsertRequestToBooking(request),
                roomId, userId);

        kafkaEventService.bookingEvent(newBooking);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingMapper.bookingToResponse(newBooking));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @AuthorizeAction(actionType = "update")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable UUID id,
                                                         @RequestBody @Valid UpsertBookingRequest request,
                                                         @RequestParam(required = false) UUID roomId) {
        Booking updatedBooking = bookingService.updateBooking(bookingMapper.upsertRequestToBooking(request),
                id, roomId);

        return ResponseEntity.ok(bookingMapper.bookingToResponse(updatedBooking));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @AuthorizeAction(actionType = "delete")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
