package com.example.booking.service.impl;

import com.example.booking.entity.BookingEvent;
import com.example.booking.entity.RegistrationEvent;
import com.example.booking.repository.BookingEventRepository;
import com.example.booking.repository.RegistrationEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsSavingService {

    private final RegistrationEventRepository registrationEventRepository;
    private final BookingEventRepository bookingEventRepository;

    public void saveToCSV() {
        int number = 0;
        try {
            FileWriter writer = new FileWriter("fileCSV.csv");

            saveRegistrationEvent(writer, number);
            saveBookingEvent(writer, number);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void saveRegistrationEvent(FileWriter writer, int number) throws IOException {
        List<RegistrationEvent> registrationEventList = registrationEventRepository.findAll();

        writer.append("RegistrationEvent: \n");
        for (RegistrationEvent registrationEvent : registrationEventList) {
            number++;
            writer.write(number + ". UserId: " + registrationEvent.getUserId().toString() + ",\n");
        }
    }

    private void saveBookingEvent(FileWriter writer, int number) throws IOException {
        List<BookingEvent> bookingEventList = bookingEventRepository.findAll();

        writer.append("BookingEvent: \n");
        for (BookingEvent bookingEvent : bookingEventList) {
            number++;
            writer.write(number + "." +
                    " UserId: " + bookingEvent.getUserId().toString() +
                    " Booking: Check-in: " + bookingEvent.getCheckIn() +
                    " Check-out: " + bookingEvent.getCheckOut() + ",\n");
        }
    }
}
