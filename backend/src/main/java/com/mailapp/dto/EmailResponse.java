package com.mailapp.dto;

import com.mailapp.model.Email;
import java.time.LocalDateTime;
import java.util.List;

public class EmailResponse {

    public String id;
    public String sender;
    public String senderEmail;
    public List<String> recipients;
    public String subject;
    public String body;
    public String preview;
    public String status;
    public boolean read;
    public boolean starred;
    public String avatar;
    public LocalDateTime createdAt;

    public static EmailResponse from(Email e) {
        EmailResponse r = new EmailResponse();
        r.id = e.getId();
        r.sender = e.getSender();
        r.senderEmail = e.getSenderEmail();
        r.recipients = e.getRecipients();
        r.subject = e.getSubject();
        r.body = e.getBody();
        r.preview = e.getPreview();
        r.status = e.getStatus();
        r.read = e.isRead();
        r.starred = e.isStarred();
        r.avatar = e.getAvatar();
        r.createdAt = e.getCreatedAt();
        return r;
    }
}