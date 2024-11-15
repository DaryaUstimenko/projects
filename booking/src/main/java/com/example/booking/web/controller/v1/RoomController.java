package com.example.booking.web.controller.v1;

import com.example.booking.entity.Room;
import com.example.booking.mapper.RoomMapper;
import com.example.booking.service.RoomService;
import com.example.booking.web.model.request.PaginationRequest;
import com.example.booking.web.model.request.RoomsFilterRequest;
import com.example.booking.web.model.request.UpsertRoomRequest;
import com.example.booking.web.model.response.ModelListResponse;
import com.example.booking.web.model.response.RoomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    private final RoomMapper roomMapper;

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ModelListResponse<RoomResponse>> filterBy(@Valid RoomsFilterRequest filter,
                                                                    @Valid PaginationRequest request) {
        filter.setPagination(request);
        Page<Room> rooms = roomService.filterBy(filter);

        return ResponseEntity.ok(
                ModelListResponse.<RoomResponse>builder()
                        .totalCount(rooms.getTotalElements())
                        .data(rooms.stream().map(roomMapper::roomToResponse).toList())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(roomMapper.roomToResponse(roomService.findById(id)));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> createRoom(@RequestParam(required = false) UUID hotelId,
                                                   @RequestBody @Valid UpsertRoomRequest request) {
        Room newRoom = roomService.addRoom(roomMapper.upsertRequestToRoom(request), hotelId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomMapper.roomToResponse(newRoom));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom( @PathVariable UUID id,
                                                    @RequestParam(required = false) UUID hotelId,
                                                    @RequestBody @Valid UpsertRoomRequest request) {
        Room updatedRoom = roomService.updateRoom(roomMapper.upsertRequestToRoom(request), id, hotelId);

        return ResponseEntity.ok(roomMapper.roomToResponse(updatedRoom));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        roomService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
