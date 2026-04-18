package com.mailapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String sender;
    private String senderEmail;

    @ElementCollection
    private List<String> recipients;

    private String subject;

    @Column(length = 10000)
    private String body;

    private String preview;

    private String status;

    private boolean isRead;
    private boolean isStarred;

    private String avatar;

    private LocalDateTime createdAt = LocalDateTime.now();
}