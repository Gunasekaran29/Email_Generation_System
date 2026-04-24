package com.mailapp.service;

import com.mailapp.dto.EmailResponse;
import com.mailapp.dto.SendEmailRequest;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmailService {

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
    // SEND EMAIL (BREVO API)
    // ─────────────────────────────
    public EmailResponse send(SendEmailRequest req) {

        if (req.getTo() == null || req.getTo().isBlank()) {
            throw new RuntimeException("Recipient required");
        }

        try {
            String json = String.format("""
            {
              "sender": {
                "email": "gsanthosh5910@gmail.com",
                "name": "Santhosh"
              },
              "to": [{"email": "%s"}],
              "subject": "%s",
              "htmlContent": "%s"
            }
            """,
            req.getTo(),
            req.getSubject() != null ? req.getSubject() : "No Subject",
            req.getBody() != null ? req.getBody() : ""
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", System.getenv("BREVO_API_KEY"))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("Brevo API failed: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Email send failed: " + e.getMessage());
        }

        // ─────────────────────────────
        // STORE IN SENT
        // ─────────────────────────────
        EmailResponse sentMail = new EmailResponse();

        sentMail.setId(UUID.randomUUID().toString());
        sentMail.setSender("Me");
        sentMail.setSenderEmail("gsanthosh5910@gmail.com");

        List<String> recipients = Arrays.stream(req.getTo().split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        sentMail.setRecipients(recipients);
        sentMail.setSubject(req.getSubject());
        sentMail.setBody(req.getBody());

        String body = req.getBody() != null ? req.getBody() : "";
        sentMail.setPreview(body.substring(0, Math.min(50, body.length())));

        sentMail.setStatus("sent");
        sentMail.setRead(true);
        sentMail.setStarred(false);
        sentMail.setAvatar("");
        sentMail.setCreatedAt(LocalDateTime.now().toString());

        // ─────────────────────────────
        // STORE IN INBOX (SIMULATION)
        // ─────────────────────────────
        EmailResponse inboxMail = new EmailResponse();

        inboxMail.setId(UUID.randomUUID().toString());
        inboxMail.setSender(req.getTo());
        inboxMail.setSenderEmail(req.getTo());
        inboxMail.setRecipients(List.of("me"));
        inboxMail.setSubject(req.getSubject());
        inboxMail.setBody(req.getBody());

        inboxMail.setPreview(body.substring(0, Math.min(50, body.length())));

        inboxMail.setStatus("inbox");
        inboxMail.setRead(false);
        inboxMail.setStarred(false);
        inboxMail.setAvatar("");
        inboxMail.setCreatedAt(LocalDateTime.now().toString());

        // ─────────────────────────────
        // SAVE BOTH
        // ─────────────────────────────
        emails.add(sentMail);
        emails.add(inboxMail);

        return sentMail;
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