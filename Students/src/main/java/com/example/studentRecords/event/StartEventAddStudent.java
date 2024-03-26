package com.example.studentRecords.event;

import com.example.studentRecords.model.Student;
import com.example.studentRecords.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(name = "app.add-students-on-start", havingValue = "true")
@RequiredArgsConstructor
public class StartEventAddStudent {
    private final StudentService studentService;

    @EventListener(ApplicationStartedEvent.class)
    public void createStudents() {
        studentService.saveStudent(Student.builder()
                .firstName("Иван")
                .lastName("Петров")
                .age(21)
                .build());
        studentService.saveStudent(Student.builder()
                .firstName("Петр")
                .lastName("Степанов")
                .age(20)
                .build());
        log.info("\nДля получения списка команд, введите: com");
    }
}
