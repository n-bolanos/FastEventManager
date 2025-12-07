#User Story
#As an organizer user, I want to reset my password so that I can regain access if I forget it. 
#Acceptance Criteria: Given the user is on the login page, When they click "Forgot Password" and enter their email, 
#Then the system sends a password reset link to the user’s email.

Feature: Password reset
  As an organizer user
  I want to reset my password
  So that I can regain access if I forget it

  @reset_success
  Scenario: Successful password reset request
    Given an existing user with email "john@example.com"
    And a password reset request with:
      | email | john@example.com |
    When the user submits the password reset request
    Then the system should send a password reset link to "john@example.com"
    And the response status should be 200
    And the response should contain message "Reset link sent"

  @reset_user_not_found
  Scenario: Password reset fails when the email does not exist
    Given no user exists with email "ghost@example.com"
    And a password reset request with:
      | email | ghost@example.com |
    When the user submits the password reset request
    Then the response status should be 404
    And the response should contain message "User not found"
