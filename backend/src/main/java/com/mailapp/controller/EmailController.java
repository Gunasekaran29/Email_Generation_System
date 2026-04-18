package com.mailapp.controller;

import com.mailapp.dto.*;
import com.mailapp.service.EmailService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/emails")
@CrossOrigin("*")
public class EmailController {

    private final EmailService svc;

    public EmailController(EmailService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<EmailResponse> getAll() {
        return svc.getAll();
    }

    @PostMapping("/send")
    public EmailResponse send(@RequestBody SendEmailRequest req) {
        return svc.send(req);
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }
}