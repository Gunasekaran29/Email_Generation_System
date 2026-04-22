package com.mailapp.service;

import com.mailapp.dto.EmailResponse;
import com.mailapp.dto.SendEmailRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmailService {

    private final List<EmailResponse> emails = new ArrayList<>();

    public List<EmailResponse> getAll() {
        return emails;
    }

    public List<EmailResponse> getByStatus(String status) {
        return emails;
    }

    public EmailResponse send(SendEmailRequest req) {

        EmailResponse res = new EmailResponse();
        res.setId(UUID.randomUUID().toString());
        res.setSender("Me");
        res.setSubject(req.getSubject());
        res.setBody(req.getBody());

        emails.add(res); // 🔥 SAVE HERE

        return res;
    }
}