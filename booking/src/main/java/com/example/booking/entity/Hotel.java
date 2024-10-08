package com.example.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldNameConstants
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String hotelName;

    private String title;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private double centerDistance;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Room> rooms = new ArrayList<>();

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private int mark;

    @Column(nullable = false)
    private int numberOFMarks;

    public void addRoom(Room room) {
        room.setHotel(this);
        rooms.add(room);
    }
}
