package com.example.studentRecords.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventHolderListener {
    @EventListener
    public void eventAddStudent(EventAddStudent addStudent) {
        log.info("Добавлен студент: " + addStudent.getStudent());
        //System.out.println("Добавлен студент: " + addStudent.getStudent());
    }

    @EventListener
    public void eventDeleteStudent(EventDeleteStudent deleteStudent) {
        log.info("Удален студент: " + deleteStudent.getID());
        //System.out.println("Удален студент: " + deleteStudent.getID());
    }
}

