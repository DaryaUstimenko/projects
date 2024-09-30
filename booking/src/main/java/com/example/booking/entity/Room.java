package com.example.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.time.Period;
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

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Booking> bookings = new ArrayList<>();

    public void addBooking(Booking booking) {
        booking.setRoom(this);
        bookings.add(booking);
    }

    @Convert(converter = PeriodConverter.class)
    public List<Period> busyDates = new ArrayList<>();
}


@Converter(autoApply = true)
class PeriodConverter implements AttributeConverter<Period, String> {

    @Override
    public String convertToDatabaseColumn(Period period) {
        if (period == null) {
            return null;
        }
        return period.toString();
    }

    @Override
    public Period convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Period.parse(dbData);
    }
}
