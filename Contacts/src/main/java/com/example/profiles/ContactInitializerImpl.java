package com.example.profiles;

import com.example.model.User;
import com.example.service.CreateContactService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class ContactInitializerImpl implements Contact {
    @Value("${classpath}")
    private PathResource fileName;
    private final CreateContactService createContact;

    public ContactInitializerImpl(CreateContactService createContact) {
        this.createContact = createContact;
    }

    @Override
    public Map<String, User> initContacts() {
        HashMap<String, User> contacts = new HashMap<>();
        try {
            Resource resource = new ClassPathResource(fileName.getFilename());
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            reader.lines().forEach(c -> {
                createContact.stringDivide(c);
                User user = new User( createContact.getFullName(), createContact.getPhoneNumber(),
                        createContact.getEmail());
                contacts.put(user.getEmail(),user);
            });
            System.out.println("Включена конфигурация: init" +
                    "\n(Для изменения конфигурации укажите в файле: " +
                    "\napplication.yaml -> spring.profiles.active:scan \n" +
                    "и перезапустите приложение)" +
                    "\nСписок контактов добавлен из файла: contacts.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contacts;
    }
}
