package com.mailapp.service;

import com.mailapp.dto.*;
import com.mailapp.model.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private final EmailRepository repo;

    public EmailService(EmailRepository repo) {
        this.repo = repo;
    }

    public List<EmailResponse> getAll() {
        return repo.findAll().stream().map(EmailResponse::from).collect(Collectors.toList());
    }

    public EmailResponse send(SendEmailRequest req) {
        Email e = new Email();
        e.setSender(req.senderName != null ? req.senderName : "Me");
        e.setSenderEmail("demo@mail.com");
        e.setRecipients(List.of(req.to));
        e.setSubject(req.subject);
        e.setBody(req.body);
        e.setPreview(req.body.substring(0, Math.min(50, req.body.length())));
        e.setStatus("sent");
        e.setRead(true);
        e.setStarred(false);

        return EmailResponse.from(repo.save(e));
    }
}