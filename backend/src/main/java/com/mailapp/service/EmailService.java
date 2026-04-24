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

    public List<EmailResponse> getAll() {
        return emails;
    }

    public List<EmailResponse> getByStatus(String status) {
        return emails.stream()
                .filter(e -> status.equalsIgnoreCase(e.getStatus()))
                .collect(Collectors.toList());
    }

    public EmailResponse send(SendEmailRequest req) {

        if (req.getTo() == null || req.getTo().isBlank()) {
            throw new RuntimeException("Recipient required");
        }

        try {
            String json = String.format("""
            {
              "sender": {"email": "gsanthosh5910@gmail.com"},
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

        // UI storage
        EmailResponse res = new EmailResponse();

        res.setId(UUID.randomUUID().toString());
        res.setSender("Me");
        res.setSenderEmail("gsanthosh5910@gmail.com");

        List<String> recipients = Arrays.stream(req.getTo().split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        res.setRecipients(recipients);

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

    public EmailResponse markRead(String id) {
        for (EmailResponse e : emails) {
            if (e.getId().equals(id)) {
                e.setRead(true);
                return e;
            }
        }
        return null;
    }

    public EmailResponse toggleStar(String id) {
        for (EmailResponse e : emails) {
            if (e.getId().equals(id)) {
                e.setStarred(!e.isStarred());
                return e;
            }
        }
        return null;
    }

    public EmailResponse trash(String id) {
        for (EmailResponse e : emails) {
            if (e.getId().equals(id)) {
                e.setStatus("trash");
                return e;
            }
        }
        return null;
    }

    public void delete(String id) {
        emails.removeIf(e -> e.getId().equals(id));
    }
}