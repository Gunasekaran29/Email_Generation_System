package com.mailapp.service;

import com.mailapp.dto.EmailResponse;
import com.mailapp.dto.SendEmailRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // 🔥 In-memory storage (temporary)
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
    // SEND EMAIL (REAL SMTP)
    // ─────────────────────────────
    public EmailResponse send(SendEmailRequest req) {

        // ✅ VALIDATE INPUT
        if (req.getTo() == null || req.getTo().isBlank()) {
            throw new RuntimeException("Recipient email is required");
        }

        // ✅ PREPARE RECIPIENTS
        String[] recipients = Arrays.stream(req.getTo().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        // 🔥 SEND REAL EMAIL (BREVO)
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            // ⚠️ MUST MATCH BREVO VERIFIED SENDER
            helper.setFrom("santhosh <gsanthosh5910@gmail.com>");

            helper.setTo(recipients);
            helper.setSubject(
                    req.getSubject() != null ? req.getSubject() : "(No Subject)"
            );

            helper.setText(
                    req.getBody() != null ? req.getBody() : "",
                    true
            );

            mailSender.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("SMTP failed: " + e.getMessage());
        }

        // ✅ STORE IN MEMORY (for UI)
        EmailResponse res = new EmailResponse();

        res.setId(UUID.randomUUID().toString());
        res.setSender("Me");
        res.setSenderEmail("gsanthosh5910@gmail.com");

        List<String> recipientList = Arrays.stream(recipients)
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
    // MARK AS READ
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
    // TOGGLE STAR
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
    // MOVE TO TRASH
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