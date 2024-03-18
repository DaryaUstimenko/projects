package com.example.service;

import com.example.model.User;
import com.example.profiles.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
public class WorkerService {
    @Value("${save.path}")
    private String path;
    private final Contact contact;
    private final CreateContactService createContactService;

    public WorkerService(Contact contact, CreateContactService createContactService) {
        this.contact = contact;
        this.createContactService = createContactService;
    }

    public void doWork() {
        workWithProfileBean();
        while (true) {
            System.out.println("Выберите действие: 1 - добавить пользователя,2 - удалить пользователя," +
                    "\n3 - показать список пользователей,4 - сохранить список контактов в файл,5 - выход");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String choice;
            try {
                choice = br.readLine();

                switch (choice) {
                    case "1" -> {
                        System.out.println("Введите имя пользователя для добавления в следующем формате:" +
                                " Иванов Иван Иванович;+890999999;someEmail@example.example");
                        String contact = br.readLine();
                        createContactService.stringDivide(contact);
                        createContactService.addContactToMap(createContactService.getFullName(),
                                createContactService.getPhoneNumber(), createContactService.getEmail());
//                        if (createContactService.correctContact) {
//                            System.out.println("Пользователь добавлен");
//                        }
                    }
                    case "2" -> {
                        System.out.println("Введите email пользователя для удаления данных пользователя:");
                        String removeContact = br.readLine();
                        createContactService.deleteContact(removeContact);
                    }
                    case "3" -> {
                        System.out.println("Список всех пользователей:");
                        createContactService.allContacts();
                    }
                    case "4" -> {
                        System.out.println("Сохранен список контактов в файл: " + path + "\n");
                        saveContactsToFile();
                    }
                    case "5" -> {
                        System.out.println("Выход из приложения.");
                        br.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Некорректный выбор. Выберите действие из списка и попробуйте снова.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void workWithProfileBean() {
        for (Map.Entry<String, User> map : contact.initContacts().entrySet()) {
            createContactService.addContactToMap(
                    map.getValue().getFullName(),
                    map.getValue().getPhoneNumber(),
                    map.getValue().getEmail());
        }
    }

    private void saveContactsToFile() {
        try {
            String saveFormat = createContactService.allContacts().replace(" | ", ";");
            Files.writeString(Path.of(path), saveFormat);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

