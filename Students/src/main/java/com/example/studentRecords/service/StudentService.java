package com.example.studentRecords.service;

import com.example.studentRecords.event.EventAddStudent;
import com.example.studentRecords.event.EventDeleteStudent;
import com.example.studentRecords.model.Student;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class StudentService {
    private final ApplicationEventPublisher eventPublisher;
    private final HashMap<Integer, Student> studentsMap = new HashMap<>();
    int id = 0;

    public StudentService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void saveStudent(Student student) {
        id++;
        student.setId(id);
        studentsMap.put(student.getId(), student);
        eventPublisher.publishEvent(new EventAddStudent(this, student));
    }

    public void getStudentById(int id) {
        if (!studentsMap.containsKey(id)) {
            System.out.println("Студент с таким id не найден");
        } else {
            Student student = studentsMap.get(id);
            System.out.println(student.toString());
        }
    }

    public void removeStudent(int id) {
        if (!studentsMap.containsKey(id)) {
            System.out.println("Студент с таким id не найден");
        } else {
            studentsMap.remove(id);
            eventPublisher.publishEvent(new EventDeleteStudent(this, id));
        }
    }

    public void studentsList() {
        StringBuilder students = new StringBuilder();
        if (studentsMap.isEmpty()) {
            System.out.println("Список студентов пуст");
        } else {
            for (Student student : studentsMap.values()) {
                students.append("\n").append(student.toString());
            }
            System.out.println("Список студентов:" + students);
        }
    }

    public void removeAll() {
        if (studentsMap.isEmpty()) {
            System.out.println("Список студентов пуст");
        } else {
            studentsMap.clear();
            System.out.println("Список студентов был очищен");
        }
    }
}
