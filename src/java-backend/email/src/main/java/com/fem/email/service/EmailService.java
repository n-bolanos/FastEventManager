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
import java.util.Objects;
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

            helper.setTo(Objects.requireNonNull(req.getTo()));
            helper.setSubject(Objects.requireNonNull(req.getSubject()));
            helper.setText(Objects.requireNonNull(htmlContent), true);

            mailSender.send(message);

            return new EmailResponse(
                true,
                UUID.randomUUID().toString(),
                null
            );

        } catch (MessagingException e) {
            return new EmailResponse(false, null,
                    "Email sending failed: " + e.getMessage());
        } catch (TemplateError e) {
            return new EmailResponse(false, null,
                    "Template processing failed: " + e.getMessage());
        } catch (RuntimeException e) {
            return new EmailResponse(false, null,
                    "Unexpected error: " + e.getMessage());
        }
    }

    protected String renderTemplate(EmailType type, Map<String, Object> params) {
        return switch (type) {
            case SUCCESSFUL_REGISTER -> TemplateEngine.render("successful_Register.html", params);
            case PASSWORD_RESET -> TemplateEngine.render("password_reset.html", params);
            case EVENT_CONFIRMATION -> TemplateEngine.render("event_confirmation.html", params);
            case WAITLIST_NOTIFICATION -> TemplateEngine.render("waitlist.html", params);
            case WAITLIST_PROMOTION -> TemplateEngine.render("waitlist_promotion.html", params);
            case CAPACITY_REACHED -> TemplateEngine.render("capacity_reached.html", params);
        };
    }
}