package com.example.service;

import com.example.model.User;
import lombok.Getter;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

@Service
@Getter
public class CreateContactService {
    private final Map<String, User> contacts = new HashMap<>();
    private String fullName;
    private String phoneNumber;
    private String email;
    public boolean correctContact = false;

    public void stringDivide(String contact) {
        String regexName = "[А-Яа-яёЁ]+\\s[А-Яа-яёЁ]+\\s[А-Яа-яёЁ]+";
        String regexPhone = "\\+\\d{9}";
        String regexEmail = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$";
        String regexString = regexName + ";" + regexPhone + ";" + regexEmail;
        contact = contact.trim();
        try {
            if (contact.isBlank() || !contact.matches(regexString)) {
                System.out.println("Пожалуйста введите данные пользователя в указанном формате");
            } else {
                fullName = contact.substring(0, contact.indexOf(";"));
                phoneNumber = contact.substring(contact.indexOf(";") + 1, contact.lastIndexOf(";"));
                email = contact.substring(contact.indexOf(phoneNumber) + 11);
            }
        } catch (InputMismatchException e) {
            e.printStackTrace();
        }
    }

    public Map<String, User> addContactToMap(String fullName, String phoneNumber, String email) {
        User user = new User(fullName, phoneNumber, email);
        if (contacts.containsKey(email)) {
            System.out.println("Пользователь с таким email уже существует, введите другие данные");
            correctContact = false;
        } else {
            contacts.put(user.getEmail(), user);
            correctContact = true;
        }
        return contacts;
    }

    public String allContacts() {
        String allContacts = "";
        for (User user : contacts.values()) {
            if (user.getFullName() != null) {
                allContacts += user.getFullName() + " | " + user.getPhoneNumber() + " | " + user.getEmail() + "\n";
            }
        }
        System.out.println(allContacts);
        return allContacts;
    }

    public void deleteContact(String email) {
        if (contacts.containsKey(email)) {
            contacts.remove(email);
            System.out.println("Пользователь удален");
        } else {
            System.out.println("Пользователь не найден");
        }
    }
}
