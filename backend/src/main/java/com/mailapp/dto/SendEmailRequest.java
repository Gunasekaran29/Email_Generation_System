package com.mailapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendEmailRequest {
    @NotBlank(message = "Recipient(s) required")
    private String to;          // comma-separated email addresses

    @NotBlank(message = "Subject required")
    private String subject;

    private String body;

    private String senderName;  // display name, defaults to "Me"
}
