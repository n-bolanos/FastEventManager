package com.fem.email.service;

import com.fem.email.dto.EmailRequest;
import com.fem.email.dto.EmailResponse;
import com.fem.email.dto.EmailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Service responsible for handling email composition and delivery.
 *
 * This class delegates template rendering to {@link TemplateEngine}, constructs
 * MIME messages, and sends them through the configured {@link JavaMailSender}.
 * It also handles and categorizes different types of failures, returning
 * structured {@link EmailResponse} objects.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    /** Mail sender used to create and send MIME email messages. */
    private final JavaMailSender mailSender;

    /** Map of EmailTypes to HTML templates' names. */
    private static final Map<EmailType, String> TEMPLATE_MAP = Map.of(
        EmailType.SUCCESSFUL_REGISTER, "successful_register.html",
        EmailType.PASSWORD_RESET, "password_reset.html",
        EmailType.EVENT_CONFIRMATION, "event_confirmation.html",
        EmailType.WAITLIST_NOTIFICATION, "waitlist.html",
        EmailType.WAITLIST_PROMOTION, "waitlist_promotion.html",
        EmailType.CAPACITY_REACHED, "capacity_reached.html"
    );

    /**
     * Sends an email based on the provided {@link EmailRequest}.
     *
     * This includes rendering the HTML template associated with the
     * email type, building the MIME message, and sending it through
     * the configured SMTP server.
     *
     * @param req the request containing recipient, subject, type, and template parameters
     * @return a structured {@link EmailResponse} indicating success or failure
     */
    public EmailResponse sendEmail(EmailRequest req) {
        try {
            String htmlContent = renderTemplate(req.getType(), req.getParams());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(Objects.requireNonNull(req.getTo()));
            helper.setSubject(Objects.requireNonNull(req.getSubject()));
            helper.setText(Objects.requireNonNull(htmlContent), true);

            mailSender.send(message);
            
            log.info("Successful email sending");
            return new EmailResponse(
                true,
                UUID.randomUUID().toString(),
                null
            );
        
        } catch (MailException | MessagingException e) {
            log.error("Email sending failed: {}", e.getMessage(), e);
            return new EmailResponse(
                false,
                null,
                "Email sending failed: " + e.getMessage()
            );

        } catch (TemplateError e) {
            log.error("Template processing failed: {}", e.getMessage(), e);
            return new EmailResponse(
                false,
                null,
                "Template processing failed: " + e.getMessage()
            );

        } catch (RuntimeException e) {
            log.error("Unexpected error while sending email: {}", e.getMessage(), e);
            return new EmailResponse(
                false,
                null,
                "Unexpected error: " + e.getMessage()
            );
        }
    }

    /**
     * Resolves and renders the appropriate email template based on the provided type.
     *
     * Each template filename is mapped to an {@link EmailType}. The rendering
     * process replaces placeholders using the given parameters.
     *
     * @param type   the type of email to render
     * @param params the parameters used to replace placeholders in the template
     * @return the rendered HTML content as a string
     */
    protected String renderTemplate(EmailType type, Map<String, Object> params) {
        String template = TEMPLATE_MAP.get(type);

        if (template == null)
            throw new TemplateError("No template configured for type: " + type);

        return TemplateEngine.render(template, params);
    }
}