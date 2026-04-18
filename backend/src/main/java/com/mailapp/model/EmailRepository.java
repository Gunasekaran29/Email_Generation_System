package com.mailapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {

    List<Email> findByStatusOrderByCreatedAtDesc(String status);

    List<Email> findByIsStarredTrueOrderByCreatedAtDesc();

    // ✅ ADD THIS (YOU MISSED)
    List<Email> findBySubjectContainingIgnoreCaseOrSenderContainingIgnoreCaseOrderByCreatedAtDesc(
            String subject,
            String sender
    );

    long countByStatusAndIsReadFalse(String status);
}