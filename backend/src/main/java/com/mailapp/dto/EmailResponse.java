package com.mailapp.dto;

import com.mailapp.model.Email;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Outbound shape — matches the React useEmails hook's field names exactly.
 */
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
    private OffsetDateTime createdAt;

    public static EmailResponse from(Email e) {
        EmailResponse r = new EmailResponse();
        r.setId(e.getId());
        r.setSender(e.getSender());
        r.setSenderEmail(e.getSenderEmail());
        r.setRecipients(e.getRecipients());
        r.setSubject(e.getSubject());
        r.setBody(e.getBody());
        r.setPreview(e.getPreview());
        r.setStatus(e.getStatus());
        r.setRead(e.isRead());
        r.setStarred(e.isStarred());
        r.setAvatar(e.getAvatar() != null ? e.getAvatar() : "");
        r.setCreatedAt(e.getCreatedAt());
        return r;
    }
}
