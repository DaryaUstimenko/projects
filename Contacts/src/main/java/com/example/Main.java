package com.example;

import com.example.config.AppConfig;
import com.example.service.WorkerService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.getBean(WorkerService.class).doWork();
    }
}
