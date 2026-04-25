package com.mailapp.service;

import com.mailapp.model.Email;
import com.mailapp.model.EmailRepository;
import com.mailapp.dto.SendEmailRequest;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailService {

    private final EmailRepository repo;

    public EmailService(EmailRepository repo) {
        this.repo = repo;
    }

    // GET ALL
    public List<Email> getAll() {
        return repo.findAll();
    }

    // FILTER
    public List<Email> getByStatus(String status) {
        return repo.findByStatus(status);
    }

    // SEND MAIL
    public Email send(SendEmailRequest req) {

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

        // SAVE SENT MAIL ONLY
        Email sent = new Email();

        sent.setSender("Me");
        sent.setSenderEmail("gsanthosh5910@gmail.com");
        sent.setRecipients(List.of(req.getTo()));
        sent.setSubject(req.getSubject());
        sent.setBody(req.getBody());

        String body = req.getBody() != null ? req.getBody() : "";
        sent.setPreview(body.substring(0, Math.min(50, body.length())));

        sent.setStatus("sent");
        sent.setRead(true);
        sent.setStarred(false);
        sent.setAvatar("");
        sent.setCreatedAt(LocalDateTime.now());

        return repo.save(sent);
    }

    // MARK READ
    public Email markRead(String id) {
        return repo.findById(id).map(e -> {
            e.setRead(true);
            return repo.save(e);
        }).orElse(null);
    }

    // STAR
    public Email toggleStar(String id) {
        return repo.findById(id).map(e -> {
            e.setStarred(!e.isStarred());
            return repo.save(e);
        }).orElse(null);
    }

    // TRASH
    public Email trash(String id) {
        return repo.findById(id).map(e -> {
            e.setStatus("trash");
            return repo.save(e);
        }).orElse(null);
    }

    // DELETE
    public void delete(String id) {
        repo.deleteById(id);
    }
}