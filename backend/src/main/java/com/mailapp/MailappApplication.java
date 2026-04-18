package com.mailapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class MailappApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailappApplication.class, args);
    }
}

@RestController
class RootController {

    @GetMapping("/")
    public String home() {
        return "RUNNING";
    }
}