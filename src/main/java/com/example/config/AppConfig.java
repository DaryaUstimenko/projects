package com.example.config;

import org.springframework.context.annotation.*;

@ComponentScan("com.example")
@Configuration
@PropertySource("classpath:application.yaml")
public class AppConfig {
}
