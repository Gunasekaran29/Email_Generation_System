package com.mailapp.service;

import com.mailapp.dto.EmailResponse;
import com.mailapp.dto.SendEmailRequest;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmailService {

    // 🔥 TEMP in-memory storage
    private final List<EmailResponse> emails = new ArrayList<>();

    // ─────────────────────────────
    // GET ALL
    // ─────────────────────────────
    public List<EmailResponse> getAll() {
        return emails;
    }

    // ─────────────────────────────
    // FILTER BY STATUS
    // ─────────────────────────────
    public List<EmailResponse> getByStatus(String status) {
        return emails.stream()
                .filter(e -> status.equalsIgnoreCase(e.getStatus()))
                .collect(Collectors.toList());
    }

    // ─────────────────────────────
    // SEND EMAIL (FORCED BREVO)
    // ─────────────────────────────
    public EmailResponse send(SendEmailRequest req) {

        if (req.getTo() == null || req.getTo().isBlank()) {
            throw new RuntimeException("Recipient required");
        }

        try {
            // 🔥 FORCE BREVO SMTP (bypass config issues)
JavaMailSenderImpl sender = new JavaMailSenderImpl();

sender.setHost("smtp-relay.brevo.com");
sender.setPort(587);
sender.setUsername(System.getenv("SPRING_MAIL_USERNAME"));
sender.setPassword(System.getenv("SPRING_MAIL_PASSWORD"));

Properties props = sender.getJavaMailProperties();

props.put("mail.transport.protocol", "smtp");
props.put("mail.smtp.auth", "true");
props.put("mail.smtp.starttls.enable", "true");
props.put("mail.smtp.starttls.required", "true");
props.put("mail.smtp.connectiontimeout", "5000");
props.put("mail.smtp.timeout", "5000");
props.put("mail.smtp.writetimeout", "5000");

MimeMessage msg = sender.createMimeMessage();
MimeMessageHelper helper = new MimeMessageHelper(msg);

helper.setFrom("gsanthosh5910@gmail.com");
helper.setTo(req.getTo().split(","));
helper.setSubject(req.getSubject());
helper.setText(req.getBody());

sender.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("SMTP failed: " + e.getMessage());
        }

        // ✅ STORE FOR UI
        EmailResponse res = new EmailResponse();

        res.setId(UUID.randomUUID().toString());
        res.setSender("Me");
        res.setSenderEmail("gsanthosh5910@gmail.com");

        List<String> recipientList = Arrays.stream(req.getTo().split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        res.setRecipients(recipientList);

        res.setSubject(req.getSubject());
        res.setBody(req.getBody());

        String body = req.getBody() != null ? req.getBody() : "";
        res.setPreview(body.substring(0, Math.min(50, body.length())));

        res.setStatus("sent");
        res.setRead(true);
        res.setStarred(false);
        res.setAvatar("");
        res.setCreatedAt(LocalDateTime.now().toString());

        emails.add(res);

        return res;
    }

    // ─────────────────────────────
    // MARK READ
    // ─────────────────────────────
    public EmailResponse markRead(String id) {
        for (EmailResponse e : emails) {
            if (e.getId().equals(id)) {
                e.setRead(true);
                return e;
            }
        }
        return null;
    }

    // ─────────────────────────────
    // STAR
    // ─────────────────────────────
    public EmailResponse toggleStar(String id) {
        for (EmailResponse e : emails) {
            if (e.getId().equals(id)) {
                e.setStarred(!e.isStarred());
                return e;
            }
        }
        return null;
    }

    // ─────────────────────────────
    // TRASH
    // ─────────────────────────────
    public EmailResponse trash(String id) {
        for (EmailResponse e : emails) {
            if (e.getId().equals(id)) {
                e.setStatus("trash");
                return e;
            }
        }
        return null;
    }

    // ─────────────────────────────
    // DELETE
    // ─────────────────────────────
    public void delete(String id) {
        emails.removeIf(e -> e.getId().equals(id));
    }
}