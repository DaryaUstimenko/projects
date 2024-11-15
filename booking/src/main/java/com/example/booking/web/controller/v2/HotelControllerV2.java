package com.example.booking.web.controller.v2;

import com.example.booking.entity.Hotel;
import com.example.booking.mapper.HotelMapper;
import com.example.booking.service.HotelService;
import com.example.booking.web.model.request.*;
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
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
public class HotelControllerV2 {

    private final HotelService hotelService;

    private final HotelMapper hotelMapper;

    @GetMapping(value = "/filter", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public String filterBy(@Valid HotelsFilterRequest filter,
                           @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                           @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                           Model model) {

        PaginationRequest paginationRequest = new PaginationRequest(pageSize, pageNumber);
        filter.setPagination(paginationRequest);
        Page<Hotel> hotels = hotelService.filterBy(filter);

        model.addAttribute("hotels", hotels);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("filter", filter);

        return "hotel/all_hotels";
    }

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public String findById(@PathVariable UUID id, Model model) {
        Hotel hotel = hotelService.findById(id);
        model.addAttribute("hotel", hotel);
        return "hotel/hotel";
    }

    @GetMapping(value = "/create", produces = MediaType.TEXT_HTML_VALUE)
    public String showCreateForm(Model model) {
        model.addAttribute("hotel", new UpsertHotelUpdateRequest());
        return "hotel/hotel_create";
    }

    @PostMapping(value = "/create", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String createHotel(@RequestBody @Valid @ModelAttribute(name = "hotel") UpsertHotelUpdateRequest hotel,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("hotel", hotel);
            return "hotel/hotel_create";
        }
        hotelService.save(hotelMapper.upsertRequestToUpdateHotel(hotel));
        return "redirect:/api/v1/hotel/filter";
    }

    @PostMapping(value = "/rating", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public String updateHotelRating(@RequestParam(value = "hotelId") UUID hotelId,
                                    @RequestParam(value = "mark") int mark, Model model) {

        Hotel hotelWithNewRating = hotelService.updateRating(hotelId, mark);
        model.addAttribute("hotel", hotelWithNewRating);
        hotelService.update(hotelId, hotelWithNewRating);
        return "hotel/rating";
    }

    @GetMapping(value = "/update/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String showUpdateForm(@PathVariable UUID id, Model model) {
        AtomicReference<String> showForm = new AtomicReference<>("");
        Hotel hotel = hotelService.findById(id);
        if (hotel != null) {
            model.addAttribute("hotel", hotel);
            showForm.set("hotel/hotel_update");
        } else {
            showForm.set("redirect:/api/v1/hotel/create");
        }
        return showForm.get();
    }

    @PostMapping(value = "/update/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String updateHotel(@PathVariable UUID id,
                              @RequestBody @Valid @ModelAttribute(name = "hotel") UpsertHotelUpdateRequest hotel,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("hotel", hotel);
            return "hotel/hotel_update";
        }
        hotelService.update(id, hotelMapper.upsertRequestToUpdateHotel(hotel));
        return "redirect:/api/v1/hotel/" + id;
    }

    @GetMapping(value = "/delete/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteById(@PathVariable UUID id) {
        hotelService.deleteById(id);

        return "redirect:/api/v1/hotel/filter";
    }
}
