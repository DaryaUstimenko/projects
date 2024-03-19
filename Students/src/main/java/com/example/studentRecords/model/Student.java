package com.example.studentRecords.model;

import lombok.*;

import java.text.MessageFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    private int Id;
    private String firstName;
    private String lastName;
    private int age;

    public String toString() {
        return MessageFormat.format("{0}  Имя:{1}  Фамилия:{2} Возраст:{3}",
                getId(), getFirstName(), getLastName(), getAge());
    }
}
