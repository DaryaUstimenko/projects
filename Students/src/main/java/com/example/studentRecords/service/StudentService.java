package com.example.studentRecords.service;

import com.example.studentRecords.event.EventAddStudent;
import com.example.studentRecords.event.EventDeleteStudent;
import com.example.studentRecords.exception.StudentNotFoundException;
import com.example.studentRecords.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class StudentService {
    private final ApplicationEventPublisher eventPublisher;
    private final HashMap<Integer, Student> studentsMap = new HashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(1);

    public StudentService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void saveStudent(Student student) {
        Integer id = currentId.getAndIncrement();
        student.setId(id);
        studentsMap.put(student.getId(), student);
        eventPublisher.publishEvent(new EventAddStudent(this, student));
    }

    public void getStudentById(int id) {
        if (!studentsMap.containsKey(id)) {
            throw new StudentNotFoundException("Студент с таким id не найден");
        } else {
            log.info(studentsMap.get(id).toString());
        }
    }

    public void removeStudent(int id) {
        if (!studentsMap.containsKey(id)) {
            throw new StudentNotFoundException("Студент с таким id не найден");
        } else {
            studentsMap.remove(id);
            eventPublisher.publishEvent(new EventDeleteStudent(this, id));
        }
    }

    public void studentsList() {
        StringBuilder students = new StringBuilder();
        if (studentsMap.isEmpty()) {
            throw new StudentNotFoundException("Список студентов пуст");
        } else {
            for (Student student : studentsMap.values()) {
                students.append("\n").append(student.toString());
            }
            log.info("Список студентов: " + students);
        }
    }

    public void removeAll() {
        if (studentsMap.isEmpty()) {
            throw new StudentNotFoundException("Список студентов пуст");
        } else {
            studentsMap.clear();
            log.info("Список студентов был очищен");
        }
    }
}
