package com.mailapp.dto;

import lombok.Data;

@Data
public class EmailResponse {
    private String id;
    private String sender;
    private String subject;
    private String body;
}