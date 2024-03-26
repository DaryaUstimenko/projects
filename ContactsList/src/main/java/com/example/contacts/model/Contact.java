package com.example.contacts.model;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class Contact {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    /*
        String regexName = "[А-Яа-яёЁ]+\\s[А-Яа-яёЁ]+\\s[А-Яа-яёЁ]+";
        String regexPhone = "\\+\\d{9}";
        String regexEmail = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$";
     */
}
