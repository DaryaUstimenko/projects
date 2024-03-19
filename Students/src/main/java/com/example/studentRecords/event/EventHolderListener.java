package com.example.studentRecords.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventHolderListener {
    @EventListener
    public void eventAddStudent(EventAddStudent addStudent) {
        System.out.println("Добавлен студент: " + addStudent.getStudent());
    }

    @EventListener
    public void eventDeleteStudent(EventDeleteStudent deleteStudent) {
        System.out.println("Удален студент: " + deleteStudent.getID());
    }
}

