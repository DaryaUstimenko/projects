package com.example.studentRecords.event;

import org.springframework.context.ApplicationEvent;

public class EventDeleteStudent extends ApplicationEvent {
    private final int id;

    public EventDeleteStudent(Object source, int id) {
        super(source);
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
