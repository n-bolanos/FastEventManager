package com.fem.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailResponse {
    private boolean success;
    private String messageId;
    private String error;
}