package com.mailapp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {

    private String to;
    private String subject;
    private String body;
}