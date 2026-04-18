package com.mailapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String sender;
    private String senderEmail;
    private String recipients;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String preview;
    private String status;

    private boolean isRead;
    private boolean isStarred;

    private String avatar;

    private LocalDateTime createdAt;
}