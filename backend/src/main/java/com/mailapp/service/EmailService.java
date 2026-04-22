package com.mailapp.service;

import com.mailapp.dto.EmailResponse;
import com.mailapp.dto.SendEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    public List<EmailResponse> getAll() {
        return new ArrayList<>();
    }

    public List<EmailResponse> getByStatus(String status) {
        return new ArrayList<>();
    }

    public EmailResponse send(SendEmailRequest req) {

        EmailResponse res = new EmailResponse();
        res.setId("1");
        res.setSubject(req.getSubject());
        res.setBody(req.getBody());
        res.setSender("Me");

        return res;
    }
}