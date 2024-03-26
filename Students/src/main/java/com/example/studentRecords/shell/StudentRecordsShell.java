package com.example.studentRecords.shell;

import com.example.studentRecords.model.Student;
import com.example.studentRecords.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class StudentRecordsShell {
    private final StudentService studentService;

    @ShellMethod(key = "save")
    public void saveStudent(
            @ShellOption(help = "Add student firstName") String firstName,
            @ShellOption(help = "Add student lastName") String lastName,
            @ShellOption(help = "Add student age") int age) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setAge(age);
        studentService.saveStudent(student);
    }

    @ShellMethod(key = "get")
    public void getStudentById(@ShellOption(help = "Add id for get student") int id) {
        studentService.getStudentById(id);
    }

    @ShellMethod(key = "rm")
    public void removeStudent(@ShellOption(help = "Add id for delete student") int id) {
        studentService.removeStudent(id);
    }

    @ShellMethod(key = "list")
    public void studentsList() {
        studentService.studentsList();
    }

    @ShellMethod(key = "rm all")
    public void removeAll() {
        studentService.removeAll();
    }

    @ShellMethod(key = "com")
    public String commandList() {
        return "Введите одну из команд:\n" +
                "Для добавления нового студента: save Имя Фамилию возраст \n" +
                "Для просмотра одного студента: get id\n" +
                "Для просмотра всех студентов: list\n" +
                "Для удаления одного студента: rm id\n" +
                "Для удаления всех студентов: rm all\n";
    }
}
