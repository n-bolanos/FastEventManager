#User Story
#As an organizer user, I want to access my account so that I can check and manage the events that I have created.
#Acceptance criteria: Given the user is on the login page, When they type their username and password and click “Continue”, 
#Then the system verifies their credentials and redirects them to the main page.


Feature: Organizer user login
  As an organizer user
  I want to access my account
  So that I can check and manage the events I have created

  @successful_login
  Scenario: Login with valid credentials
    Given a user with username "organizer1" and password "mypassword" exists
    And a login request with:
      | username | organizer1 |
      | password | mypassword |
    When the user submits the login request
    Then the system should authenticate the user
    And the response should contain a token

  @failed_login
  Scenario: Login with invalid credentials
    Given a login request with:
      | username | organizer1 |
      | password | wrongpass  |
    When the user submits the login request
    Then the response status should be 409
    And the response should contain message "Invalid credentials"
