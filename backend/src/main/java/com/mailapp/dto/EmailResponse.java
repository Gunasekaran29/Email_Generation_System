package com.mailapp.dto;

import com.mailapp.model.Email;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EmailResponse {

    private String id;
    private String sender;
    private String senderEmail;
    private List<String> recipients;
    private String subject;
    private String body;
    private String preview;
    private String status;
    private boolean read;
    private boolean starred;
    private String avatar;
    private LocalDateTime createdAt;

    public static EmailResponse from(Email e) {
        return EmailResponse.builder()
                .id(e.getId())
                .sender(e.getSender())
                .senderEmail(e.getSenderEmail())
                .recipients(e.getRecipients())
                .subject(e.getSubject())
                .body(e.getBody())
                .preview(e.getPreview())
                .status(e.getStatus())
                .read(e.isRead())
                .starred(e.isStarred())
                .avatar(e.getAvatar())
                .createdAt(e.getCreatedAt())
                .build();
    }
}