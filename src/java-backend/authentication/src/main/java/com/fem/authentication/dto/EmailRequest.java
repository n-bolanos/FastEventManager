package com.fem.authentication.dto;

import java.util.Map; 
 
public record EmailRequest(
    String to, 
    String subject, 
    EmailType type,
    Map<String, Object> params) {
}