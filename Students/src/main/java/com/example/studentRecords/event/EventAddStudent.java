package com.example.studentRecords.event;

import com.example.studentRecords.model.Student;
import org.springframework.context.ApplicationEvent;

public class EventAddStudent extends ApplicationEvent {
    private final Student student;

    public EventAddStudent(Object source, Student student) {
        super(source);
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}

