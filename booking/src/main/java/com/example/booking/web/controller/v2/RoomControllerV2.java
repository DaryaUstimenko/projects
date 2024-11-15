package com.example.booking.web.controller.v2;

import com.example.booking.entity.Room;
import com.example.booking.mapper.RoomMapper;
import com.example.booking.service.RoomService;
import com.example.booking.web.model.request.PaginationRequest;
import com.example.booking.web.model.request.RoomsFilterRequest;
import com.example.booking.web.model.request.UpsertRoomRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomControllerV2 {

    private final RoomService roomService;

    private final RoomMapper roomMapper;

    @GetMapping(value = "/filter", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public String filterBy(
            @Valid RoomsFilterRequest filter,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            Model model) {

        PaginationRequest paginationRequest = new PaginationRequest(pageSize, pageNumber);

        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", pageNumber);
        filter.setPagination(paginationRequest);
        model.addAttribute("filter", filter);
        Page<Room> rooms = roomService.filterBy(filter);
        model.addAttribute("rooms", rooms);

        return "room/all_rooms";
    }

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public String findById(@PathVariable UUID id, Model model) {
        Room room = roomService.findById(id);
        model.addAttribute("room", room);
        model.addAttribute("hotelId", room.getHotel().getId());
        return "room/room";
    }

    @GetMapping(value = "/create", produces = MediaType.TEXT_HTML_VALUE)
    public String showCreateForm(Model model) {
        model.addAttribute("room", new UpsertRoomRequest());
        return "room/room_create";
    }

    @PostMapping(value = "/create", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String createRoom(@RequestParam UUID hotelId,
                             @RequestBody @Valid @ModelAttribute(name = "room") UpsertRoomRequest room,
                            BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("room", room);
            return "room/room_create";
        }
        roomService.addRoom(roomMapper.upsertRequestToRoom(room), hotelId);
        return "redirect:/api/v1/room/filter";
    }

    @GetMapping(value = "/update/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String showUpdateForm(@PathVariable UUID id, Model model) {
        AtomicReference<String> showForm = new AtomicReference<>("");
        Room room = roomService.findById(id);
        if (room != null) {
            model.addAttribute("room", room);
            showForm.set("room/room_update");
        } else {
            showForm.set("redirect:/api/v1/room/create");
        }
        return showForm.get();
    }

    @PostMapping(value = "/update/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String updateRoom(@PathVariable UUID id,
            @RequestBody @Valid @ModelAttribute(name = "room")  UpsertRoomRequest room,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("room", room);
            return "room/room_update";
        }
        roomService.update(id, roomMapper.upsertRequestToRoom(room));
        return  "redirect:/api/v1/room/" + id;
    }

    @GetMapping(value = "/delete/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteById(@PathVariable UUID id) {
        roomService.deleteById(id);

        return "redirect:/api/v1/room/filter";
    }
}
