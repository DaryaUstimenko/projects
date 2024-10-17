package com.example.booking.repository;

import com.example.booking.entity.RegistrationEvent;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegistrationEventRepository extends MongoRepository<RegistrationEvent, ObjectId> {
}
