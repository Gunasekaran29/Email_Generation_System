package com.mailapp.dto;

import lombok.Data;
import java.util.List;

@Data
public class EmailResponse {

    private String id;
    private String sender;
    private String senderEmail;
    private List<String> recipients; // 🔥 IMPORTANT
    private String subject;
    private String body;
    private String preview;
    private String status;
    private boolean isRead;
    private boolean isStarred;
    private String avatar;
    private String createdAt;
}