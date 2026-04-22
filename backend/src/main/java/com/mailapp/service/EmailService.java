package com.mailapp.service;

import com.mailapp.dto.EmailResponse;
import com.mailapp.dto.SendEmailRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmailService {

    // 🔥 in-memory storage
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
    // SEND EMAIL
    // ─────────────────────────────
    public EmailResponse send(SendEmailRequest req) {

        EmailResponse res = new EmailResponse();

        res.setId(UUID.randomUUID().toString());
        res.setSender("Me");
        res.setSenderEmail("me@mail.com");

        // ✅ SAFE recipients (fix join error)
        List<String> recipients = new ArrayList<>();
        if (req.getTo() != null && !req.getTo().isBlank()) {
            recipients = Arrays.stream(req.getTo().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
        res.setRecipients(recipients);

        res.setSubject(req.getSubject());
        res.setBody(req.getBody());

        // preview
        String body = req.getBody() != null ? req.getBody() : "";
        res.setPreview(body.substring(0, Math.min(50, body.length())));

        res.setStatus("sent");
        res.setRead(true);
        res.setStarred(false);
        res.setAvatar("");
        res.setCreatedAt(LocalDateTime.now().toString());

        emails.add(res); // 🔥 store

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