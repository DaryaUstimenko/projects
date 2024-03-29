package com.example.config;

import com.example.profiles.Contact;
import com.example.profiles.ContactScannerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("scan")
public class ScanConfig {
    @Bean
    public Contact contact() {
        return new ContactScannerImpl();
    }
}