package com.fem.email.service;

import com.fem.email.dto.EmailRequest;
import com.fem.email.dto.EmailResponse;
import com.fem.email.dto.EmailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailResponse sendEmail(EmailRequest req) {
        try {
            String htmlContent = renderTemplate(req.getType(), req.getParams());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(req.getTo());
            helper.setSubject(req.getSubject());
            helper.setText(htmlContent, true);

            mailSender.send(message);

            return new EmailResponse(
                true,
                UUID.randomUUID().toString(),
                null
            );

        } catch (Exception e) {
            return new EmailResponse(false, null, e.getMessage());
        }
    }

    private String renderTemplate(EmailType type, Map<String, Object> params) {
        return switch (type) {
            case PASSWORD_RESET -> SimpleTemplateEngine.render("password_reset.html", params);
            case EVENT_CONFIRMATION -> SimpleTemplateEngine.render("event_confirmation.html", params);
            case WAITLIST_NOTIFICATION -> SimpleTemplateEngine.render("waitlist.html", params);
            case WAITLIST_PROMOTION -> SimpleTemplateEngine.render("waitlist_promotion.html", params);
            default -> throw new IllegalArgumentException("Unknown template type");
        };
    }
}