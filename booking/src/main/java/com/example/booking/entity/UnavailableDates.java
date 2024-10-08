package com.example.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldNameConstants
public class UnavailableDates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate busyFrom;

    @Column(nullable = false)
    private LocalDate busyTo;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @ToString.Exclude
    private Room room;

}
