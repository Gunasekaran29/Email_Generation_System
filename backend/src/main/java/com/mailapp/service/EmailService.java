package com.mailapp.service;

import com.mailapp.dto.EmailResponse;
import com.mailapp.dto.SendEmailRequest;
import com.mailapp.model.Email;
import com.mailapp.model.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository repo;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public List<EmailResponse> getAll() {
        return repo.findAll().stream().map(EmailResponse::from).toList();
    }

    public EmailResponse send(SendEmailRequest req) throws Exception {

        List<String> recipients = Arrays.stream(req.getTo().split(","))
                .map(String::trim)
                .toList();

        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setFrom(fromEmail);
        helper.setTo(recipients.toArray(new String[0]));
        helper.setSubject(req.getSubject());
        helper.setText(req.getBody(), true);

        mailSender.send(msg);

        Email email = repo.save(
                Email.builder()
                        .sender("Me")
                        .senderEmail(fromEmail)
                        .recipients(recipients)
                        .subject(req.getSubject())
                        .body(req.getBody())
                        .preview(req.getBody().substring(0, Math.min(50, req.getBody().length())))
                        .status("sent")
                        .isRead(true)
                        .isStarred(false)
                        .build()
        );

        return EmailResponse.from(email);
    }
}