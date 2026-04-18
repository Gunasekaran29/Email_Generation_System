package com.mailapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {

    // Get emails by status (inbox, sent, trash)
    List<Email> findByStatusOrderByCreatedAtDesc(String status);

    // Get starred emails
    List<Email> findByIsStarredTrueOrderByCreatedAtDesc();

    // Search emails
    List<Email> findBySubjectContainingIgnoreCaseOrSenderContainingIgnoreCaseOrderByCreatedAtDesc(
            String subject,
            String sender
    );

    // Unread count
    long countByStatusAndIsReadFalse(String status);
}