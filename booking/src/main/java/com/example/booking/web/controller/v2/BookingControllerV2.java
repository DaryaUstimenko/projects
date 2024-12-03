package com.example.booking.web.controller.v2;

import com.example.booking.aop.AuthorizeAction;
import com.example.booking.entity.Booking;
import com.example.booking.entity.User;
import com.example.booking.exception.AlreadyDateBusyException;
import com.example.booking.mapper.BookingMapper;
import com.example.booking.service.BookingService;
import com.example.booking.service.UserService;
import com.example.booking.service.KafkaEventService;
import com.example.booking.web.model.request.PaginationRequest;
import com.example.booking.web.model.request.UpsertBookingRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Controller
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingControllerV2 {

    private final BookingService bookingService;

    private final BookingMapper bookingMapper;

    private final UserService userService;

    private final KafkaEventService kafkaEventService;

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String findAll(
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber
            , Model model) {
        PaginationRequest request = new PaginationRequest(pageSize, pageNumber);
        Page<Booking> bookings = bookingService.findAll(request.pageRequest());
        model.addAttribute("bookings", bookings);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", pageNumber);
        return "booking/all_bookings";
    }

    @GetMapping(value = "/profile", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public String findProfileAllBookings(
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            Principal principal, Model model) {

        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
            Page<Booking> bookings = bookingService.findAllByUser(user, pageRequest);

            model.addAttribute("bookings", bookings);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("pageNumber", pageNumber);
            return "booking/personal_bookings";
        }
        return "redirect:/api/v1/user/profile";
    }

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @AuthorizeAction(actionType = "findById")
    public String findById(@PathVariable UUID id, Model model) {
        Booking booking = bookingService.findById(id);
        model.addAttribute("booking", booking);
        return "booking/booking";
    }

    @GetMapping(value = "/create", produces = MediaType.TEXT_HTML_VALUE)
    public String showCreateForm(Model model) {
        model.addAttribute("booking", new UpsertBookingRequest());
        return "booking/booking_create";
    }

    @PostMapping(value = "/create", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public String createBooking(Principal principal, @RequestParam(name = "roomId") UUID roomId,
                                @RequestBody @ModelAttribute UpsertBookingRequest booking, Model model) throws JsonProcessingException {
        try {
            if (principal != null) {
                model.addAttribute("booking", booking);
                String username = principal.getName();
                User user = userService.findByUsername(username);
                Booking newBooking = bookingService.addBooking(
                        bookingMapper.upsertRequestToBooking(booking),
                        roomId, user.getId());
                bookingService.setAllUnavailableDates(newBooking);
                if(newBooking != null) {
                    kafkaEventService.bookingEvent(newBooking);
                }
            }
            return "redirect:/api/v1/booking/profile";
        } catch (AlreadyDateBusyException ex) {
            model.addAttribute("errorMessage", ex.getMessage());

            return "booking/booking_create";
        }
    }

    @GetMapping("/unavailable-dates")
    @ResponseBody
    public List<LocalDate> getUnavailableDates() {
        return bookingService.getAllUnavailableDates();
    }

    @GetMapping(value = "/update/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String showUpdateForm(@PathVariable UUID id, Model model) {
        AtomicReference<String> showForm = new AtomicReference<>("");
        Booking booking = bookingService.findById(id);

        if (booking != null) {
            model.addAttribute("booking", booking);
            showForm.set("booking/booking_update");
        } else {
            showForm.set("redirect:/api/v1/booking/create");
        }
        return showForm.get();
    }

    @PostMapping(value = "/update/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @AuthorizeAction(actionType = "update")
    public String updateBooking(@PathVariable UUID id,
                                @RequestBody @ModelAttribute UpsertBookingRequest booking,
                                @RequestParam(required = false) UUID roomId, Model model) {

        Booking updateBooking = bookingService.updateBooking(
                bookingMapper.upsertRequestToBooking(booking), id, roomId);
        model.addAttribute("booking", updateBooking);
        return "redirect:/api/v1/booking/profile";
    }

    @GetMapping(value = "/delete/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @AuthorizeAction(actionType = "delete")
    public String deleteById(@PathVariable UUID id) {

        bookingService.deleteBooking(id);

        return "redirect:/api/v1/booking/profile";
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }
}
