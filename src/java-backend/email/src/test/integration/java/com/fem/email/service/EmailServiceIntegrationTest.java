package com.fem.email.service;

import com.fem.email.dto.EmailRequest;
import com.fem.email.dto.EmailResponse;
import com.fem.email.dto.EmailType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Integration tests for the EmailService class.
 * Tests the email sending functionality including template rendering and message delivery.
 * Does not start a Spring context; instead creates dependencies manually for isolation.
 */
@SuppressWarnings("null")
@DisplayName("EmailService Integration Tests")
class EmailServiceIntegrationTest {

	/**
	 * Creates a test fixture for a successful registration email request.
	 * 
	 * @return an EmailRequest configured for a SUCCESSFUL_REGISTER email type
	 */
	private EmailRequest createValidEmailRequest() {
		EmailRequest request = new EmailRequest();
		request.setTo("user@example.com");
		request.setSubject("Welcome");
		request.setType(EmailType.SUCCESSFUL_REGISTER);
		request.setParams(Map.of("name", "John Doe"));
		return request;
	}

	/**
	 * Creates a test fixture for an event confirmation email request.
	 * 
	 * @return an EmailRequest configured for an EVENT_CONFIRMATION email type with event details
	 */
	private EmailRequest createEventConfirmationRequest() {
		EmailRequest request = new EmailRequest();
		request.setTo("attendee@example.com");
		request.setSubject("Event Confirmation");
		request.setType(EmailType.EVENT_CONFIRMATION);
		request.setParams(Map.of(
			"name", "Jane Smith",
			"eventName", "Annual Conference",
			"eventDate", "2024-12-15",
			"location", "Convention Center"
		));
		return request;
	}

	/**
	 * Creates a test fixture for a password reset email request.
	 * 
	 * @return an EmailRequest configured for a PASSWORD_RESET email type with reset token
	 */
	private EmailRequest createPasswordResetRequest() {
		EmailRequest request = new EmailRequest();
		request.setTo("user@example.com");
		request.setSubject("Password Reset");
		request.setType(EmailType.PASSWORD_RESET);
		request.setParams(Map.of(
			"name", "John Doe",
			"token", "abc123xyz789token"
		));
		return request;
	}

	/**
	 * Creates a test fixture for a waitlist notification email request.
	 * 
	 * @return an EmailRequest configured for a WAITLIST_NOTIFICATION email type
	 */
	private EmailRequest createWaitlistNotificationRequest() {
		EmailRequest request = new EmailRequest();
		request.setTo("waitlisted@example.com");
		request.setSubject("You're on the Waitlist");
		request.setType(EmailType.WAITLIST_NOTIFICATION);
		request.setParams(Map.of(
			"name", "Bob Wilson",
			"eventName", "Exclusive Workshop"
		));
		return request;
	}

	/**
	 * Creates a test fixture for a waitlist promotion email request.
	 * 
	 * @return an EmailRequest configured for a WAITLIST_PROMOTION email type with event details
	 */
	private EmailRequest createWaitlistPromotionRequest() {
		EmailRequest request = new EmailRequest();
		request.setTo("promoted@example.com");
		request.setSubject("Spot Available - You're Invited!");
		request.setType(EmailType.WAITLIST_PROMOTION);
		request.setParams(Map.of(
			"name", "Alice Johnson",
			"eventName", "Exclusive Workshop",
			"eventDate", "2025-01-20",
			"location", "Tech Center"
		));
		return request;
	}    /**
     * Verifies that the email service successfully sends an email using
     * a valid fixture-provided {@link EmailRequest}. Ensures that the
     * response is marked successful and a message ID is generated.
     */
    @Test
    @DisplayName("sendEmail works with valid fixture from config")
    void sendEmail_WithValidFixture_ReturnsSuccess() throws Exception {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        Session session = Session.getInstance(new Properties());
        MimeMessage mimeMessage = new MimeMessage(session);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        EmailService service = new EmailService(mailSender);

        EmailResponse resp = service.sendEmail(createValidEmailRequest());

        assertNotNull(resp);
        assertTrue(resp.isSuccess(), () -> "Expected sendEmail to succeed but got error: " + resp.getError());
        assertNotNull(resp.getMessageId());

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    /**
     * Ensures that sending an event confirmation email results in a 
     * successful {@link EmailResponse}, validating template rendering 
     * and email dispatch behavior for this fixture.
     */
    @Test
    @DisplayName("sendEmail works with event confirmation fixture")
    void sendEmail_EventConfirmation_ReturnsSuccess() throws Exception {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        Session session = Session.getInstance(new Properties());
        MimeMessage mimeMessage = new MimeMessage(session);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        EmailService service = new EmailService(mailSender);

        EmailResponse resp = service.sendEmail(createEventConfirmationRequest());

        assertNotNull(resp);
        assertTrue(resp.isSuccess(), () -> "Expected sendEmail to succeed but got error: " + resp.getError());
        assertNotNull(resp.getMessageId());

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    /**
     * Validates that a password reset email can be rendered and sent 
     * successfully, ensuring that the fixture parameters and template 
     * produce correct output and mail dispatch is executed.
     */
    @Test
    @DisplayName("sendEmail works with password reset fixture")
    void sendEmail_PasswordReset_ReturnsSuccess() throws Exception {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        Session session = Session.getInstance(new Properties());
        MimeMessage mimeMessage = new MimeMessage(session);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        EmailService service = new EmailService(mailSender);

        EmailResponse resp = service.sendEmail(createPasswordResetRequest());

        assertNotNull(resp);
        assertTrue(resp.isSuccess(), () -> "Expected sendEmail to succeed but got error: " + resp.getError());
        assertNotNull(resp.getMessageId());

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    /**
     * Tests that the service properly handles the waitlist notification
     * fixture, generating a successful email response and triggering a 
     * mail send operation.
     */
    @Test
    @DisplayName("sendEmail works with waitlist notification fixture")
    void sendEmail_WaitlistNotification_ReturnsSuccess() throws Exception {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        Session session = Session.getInstance(new Properties());
        MimeMessage mimeMessage = new MimeMessage(session);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        EmailService service = new EmailService(mailSender);

        EmailResponse resp = service.sendEmail(createWaitlistNotificationRequest());

        assertNotNull(resp);
        assertTrue(resp.isSuccess(), () -> "Expected sendEmail to succeed but got error: " + resp.getError());
        assertNotNull(resp.getMessageId());

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    /**
     * Confirms that sending a waitlist promotion email via the service 
     * returns a successful response and triggers an outbound mail 
     * operation through the mocked mail sender.
     */
    @Test
    @DisplayName("sendEmail works with waitlist promotion fixture")
    void sendEmail_WaitlistPromotion_ReturnsSuccess() throws Exception {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        Session session = Session.getInstance(new Properties());
        MimeMessage mimeMessage = new MimeMessage(session);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        EmailService service = new EmailService(mailSender);

        EmailResponse resp = service.sendEmail(createWaitlistPromotionRequest());

        assertNotNull(resp);
        assertTrue(resp.isSuccess(), () -> "Expected sendEmail to succeed but got error: " + resp.getError());
        assertNotNull(resp.getMessageId());

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    /**
     * Ensures that the template rendering process throws a {@link TemplateError}
     * when required placeholders are missing from the provided parameter map.
     */
    @Test
    @DisplayName("renderTemplate throws when placeholder missing")
    void renderTemplate_MissingPlaceholder_ThrowsTemplateError() {
        assertThrows(TemplateError.class, () -> {
            TemplateEngine.render("successful_register.html", Map.of("name", "Bob", "invalid", "param"));
        });
    }

    /**
     * Confirms that the template engine correctly injects parameter values into the
     * template, replacing placeholders with the expected content.
     */
    @Test
    @DisplayName("renderTemplate replaces placeholders correctly")
    void renderTemplate_ReplacesPlaceholders() {
        String rendered = TemplateEngine.render("successful_register.html", Map.of("name", "Bob"));
        assertTrue(rendered.contains("Bob"));
    }
}
