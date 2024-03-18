package com.example.config;

import com.example.profiles.Contact;
import com.example.profiles.ContactInitializerImpl;
import com.example.service.CreateContactService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("init")
public class InitConfig {

    @Bean
public Contact contact(){
        return  new ContactInitializerImpl(new  CreateContactService());
    }
}
