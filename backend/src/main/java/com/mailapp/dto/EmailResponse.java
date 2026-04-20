package com.mailapp.dto;

import com.mailapp.model.Email;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EmailResponse {

    private String id;
    private String sender;
    private String senderEmail;
    private List<String> recipients;
    private String subject;
    private String body;
    private String preview;
    private String status;
    private boolean isRead;
    private boolean isStarred;
    private String avatar;
    private LocalDateTime createdAt;

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
        r.isRead = e.isRead();
        r.isStarred = e.isStarred();
        r.avatar = e.getAvatar();
        r.createdAt = e.getCreatedAt();
        return r;
    }
}