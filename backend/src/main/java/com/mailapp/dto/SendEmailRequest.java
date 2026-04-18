package com.mailapp.dto;

import lombok.Data;

@Data
public class SendEmailRequest {
    private String to;
    private String subject;
    private String body;
    private String senderName;
}