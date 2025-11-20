/**
 * A strategy for verifying user credentials.
 * Different authentication mechanisms (email, username)
 * can implement this interface.
 */

package com.fem.authentication.service.strategy;

import com.fem.authentication.dto.LoginRequest;
import com.fem.authentication.entity.User;
import java.util.Optional;

/**
 * Strategy for credential verification.
 *
 * Each implementation decides whether it supports a given LoginRequest
 * and attempts to authenticate returning an Optional<User>.
 */
public interface CredentialStrategy {
    /**
     * Whether this strategy can handle the given login request.
     */
    boolean supports(LoginRequest req);

    /**
     * Attempt to authenticate. Return Optional.of(user) on success or Optional.empty().
     */
    Optional<User> authenticate(LoginRequest req);
}
