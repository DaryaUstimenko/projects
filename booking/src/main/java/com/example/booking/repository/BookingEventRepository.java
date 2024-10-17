package com.example.booking.repository;

import com.example.booking.entity.BookingEvent;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingEventRepository extends MongoRepository<BookingEvent, ObjectId> {
}
