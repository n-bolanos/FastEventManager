package com.fem.email.dto;

import lombok.Data;
import java.util.Map;

@Data
public class EmailRequest {
    private String to;
    private String subject;
    private EmailType type;
    private Map<String, Object> params;
}