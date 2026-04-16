package com.mailapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {

    List<Email> findByStatusOrderByCreatedAtDesc(String status);

    List<Email> findByIsStarredTrueOrderByCreatedAtDesc();

    @Query("""
        SELECT e FROM Email e
        WHERE LOWER(e.subject) LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(e.sender)  LIKE LOWER(CONCAT('%', :q, '%'))
        ORDER BY e.createdAt DESC
    """)
    List<Email> search(@Param("q") String query);

    long countByStatusAndIsReadFalse(String status);
}
