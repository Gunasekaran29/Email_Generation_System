package com.mailapp.service;

import com.mailapp.dto.EmailResponse;
import com.mailapp.dto.SendEmailRequest;
import com.mailapp.model.Email;
import com.mailapp.model.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

private final EmailRepository repo;
private final JavaMailSender mailSender;

@Value("${spring.mail.username}")
private String fromEmail;

@Value("${app.mail.from-name:Mail App}")
private String fromName;

// ── FETCH ──────────────────────────────────────────────────────────────────

public List<EmailResponse> getAll() {
    return repo.findAll().stream().map(EmailResponse::from).collect(Collectors.toList());
}

public List<EmailResponse> getByStatus(String status) {
    return repo.findByStatusOrderByCreatedAtDesc(status)
            .stream().map(EmailResponse::from).collect(Collectors.toList());
}

public List<EmailResponse> getStarred() {
    return repo.findByIsStarredTrueOrderByCreatedAtDesc()
            .stream().map(EmailResponse::from).collect(Collectors.toList());
}

public EmailResponse getById(String id) {
    return repo.findById(id)
            .map(EmailResponse::from)
            .orElseThrow(() -> new RuntimeException("Email not found: " + id));
}

public List<EmailResponse> search(String query) {
    return repo.search(query).stream().map(EmailResponse::from).collect(Collectors.toList());
}

public long unreadCount() {
    return repo.countByStatusAndIsReadFalse("inbox");
}

// ── SEND (real SMTP delivery) ──────────────────────────────────────────────

@Transactional
public EmailResponse send(SendEmailRequest req) throws MessagingException {
    List<String> recipients = Arrays.stream(req.getTo().split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());

    dispatchSmtp(recipients, req.getSubject(), req.getBody());
    log.info("Email dispatched → {}", recipients);

    String preview = req.getBody() != null
            ? req.getBody().substring(0, Math.min(80, req.getBody().length()))
            : "";

    String display = (req.getSenderName() != null && !req.getSenderName().isBlank())
            ? req.getSenderName() : "Me";

    Email saved = repo.save(Email.builder()
            .sender(display)
            .senderEmail(fromEmail)
            .recipients(recipients)
            .subject(req.getSubject())
            .body(req.getBody())
            .preview(preview)
            .status("sent")
            .isRead(true)
            .isStarred(false)
            .avatar("")
            .build());

    return EmailResponse.from(saved);
}

private void dispatchSmtp(List<String> recipients, String subject, String body)
        throws MessagingException {

    MimeMessage msg = mailSender.createMimeMessage();
    MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");

    try {
        h.setFrom(new InternetAddress(fromEmail, fromName));
    } catch (java.io.UnsupportedEncodingException e) {
        throw new RuntimeException("Error setting sender name", e);
    }

    h.setTo(recipients.toArray(new String[0]));
    h.setSubject(subject);

    boolean isHtml = body != null &&
            (body.contains("<html") || body.contains("<p>") || body.contains("<br"));
    h.setText(body != null ? body : "", isHtml);

    mailSender.send(msg);
}

// ── MUTATIONS ──────────────────────────────────────────────────────────────

@Transactional
public EmailResponse markRead(String id) {
    Email e = getEntity(id);
    e.setRead(true);
    return EmailResponse.from(repo.save(e));
}

@Transactional
public EmailResponse toggleStar(String id) {
    Email e = getEntity(id);
    e.setStarred(!e.isStarred());
    return EmailResponse.from(repo.save(e));
}

@Transactional
public EmailResponse trash(String id) {
    Email e = getEntity(id);
    e.setStatus("trash");
    return EmailResponse.from(repo.save(e));
}

@Transactional
public void delete(String id) {
    repo.deleteById(id);
}

private Email getEntity(String id) {
    return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Email not found: " + id));
}


}
