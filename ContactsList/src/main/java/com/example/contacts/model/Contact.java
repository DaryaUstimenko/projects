package com.example.contacts.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldNameConstants;

//Замена аннотации @Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class Contact {
    private Long id;
    //Аннотации валидации. Использование аннотаций проверки (@NotNull, @Email, @Pattern и т. д.)
    @NotBlank(message = "Имя должно быть заполнено")
    @Pattern(regexp = "\\s*[А-Яа-яёЁ]+\\s*", message = "Напишите имя на русском")
    private String firstName;

    @NotBlank(message = "Фамилия должна быть заполнена")
    @Pattern(regexp = "\\s*[А-Яа-яёЁ]+\\s*", message = "Напишите имя на русском")
    private String lastName;

    @NotBlank(message = "email должен быть указан и соответствовать формату: ")
    @Email(message = "email не соответствует формату")
    private String email;

    @NotBlank(message = "Номер телефона должен быть указан")
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = "Номер телефона не соответствует формату")
    private String phone;
}
