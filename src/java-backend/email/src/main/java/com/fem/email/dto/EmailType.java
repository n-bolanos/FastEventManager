package com.fem.email.dto;

/**
 * Defines the different categories of emails that can be sent by the system.
 * 
 * Each value represents a specific template and purpose within the
 * Fast Event Manager platform.
 * 
 */
public enum EmailType {

    /** Sent when an event organizer successfully completes registration. */
    SUCCESSFUL_REGISTER,

    /** Sent when a user requests a password reset and needs a recovery link. */
    PASSWORD_RESET,

    /** Sent to confirm a user's registration for an event. */
    EVENT_CONFIRMATION,

    /** Notifies a user that they have been added to an event’s waitlist. */
    WAITLIST_NOTIFICATION,

    /**
     * Notifies a user that they have been promoted from the waitlist
     * and now have an available spot in the event.
     */
    WAITLIST_PROMOTION,

    /** Notifies an organizer that an event has reached its maximum capacity. */
    CAPACITY_REACHED
}