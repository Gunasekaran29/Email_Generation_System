
package com.mailapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmailRepository extends JpaRepository<Email, String> {

    List<Email> findByStatus(String status);

}