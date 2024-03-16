package com.example.profiles;

import com.example.model.User;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class ContactScannerImpl implements Contact {

    @Override
    public Map<String, User> initContacts() {
        System.out.println("Включена конфигурация: scan " +
                "\n(Для изменения конфигурации укажите в файле: " +
                "\napplication.yaml -> spring.profiles.active:init \n" +
                "и перезапустите приложение)" +
                "\nДля добавления контактов воспользуйтесь консолью");
        return new HashMap<>();
    }
}