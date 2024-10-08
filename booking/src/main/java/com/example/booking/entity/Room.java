package com.example.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldNameConstants
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @ToString.Exclude
    private Hotel hotel;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int maxCountGuests;

    private LocalDate busyFrom;

    private LocalDate busyTo;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<UnavailableDates> unavailableDates = new ArrayList<>();

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Booking> bookings = new ArrayList<>();

    public void addBooking(Booking booking) {
        booking.setRoom(this);
        bookings.add(booking);
    }

    public void addUnavailableDates(UnavailableDates unavailableDates) {
        unavailableDates.setRoom(this);
        this.unavailableDates.add(unavailableDates);
    }
}