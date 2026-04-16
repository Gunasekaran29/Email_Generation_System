package com.mailapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Maps to the 'emails' table in Supabase (PostgreSQL).
 *
 * Run supabase-schema.sql in your Supabase SQL Editor to create the table.
 */
@Entity
@Table(name = "emails")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String sender;

    @Column(name = "sender_email", nullable = false)
    private String senderEmail;

    /** PostgreSQL text[] — multiple recipients */
    @Column(columnDefinition = "text[]")
    private List<String> recipients;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(columnDefinition = "TEXT")
    private String preview;

    /** inbox | sent | trash */
    @Column(nullable = false)
    private String status;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "is_starred")
    private boolean isStarred;

    private String avatar;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}
