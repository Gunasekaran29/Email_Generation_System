package com.mailapp.dto;

import com.mailapp.model.Email;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Outbound shape — matches the React useEmails hook's field names exactly.
 */
@Data
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
