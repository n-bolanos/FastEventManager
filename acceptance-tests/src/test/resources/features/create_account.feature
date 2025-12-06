#User Story
#As a new organizer user, I want to create a new account so that I can create events and manage attendance.
#Acceptance criteria: Given the user in on the login page, When they click on “Create new account” 
#and fill the form, Then the system creates the account in the database and sends the user a confirmation email.

Feature: User registration
  As a new organizer user
  I want to create a new account
  So that I can log in and create events

  @successful
  Scenario: Successful user registration
    Given a registration request with:
      | name     | John Doe      |
      | username | johndoe       |
      | email    | john@example.com |
      | password | secret123     |
    And the email "john@example.com" is not registered
    And the username "johndoe" is not taken
    When the user submits the registration request
    Then the system should create the user in the database
    And the system should send a confirmation email to "john@example.com"

  @email_taken
    Scenario: Registration fails when email already exists
    Given a registration request with:
        | name     | John  |
        | username | john1 |
        | email    | john@example.com |
        | password | 12345 |
    And the user already exists with email "john@example.com" 
    When the client sends the registration request
    Then the response status should be 409
    And the response should contain message "Email already used"


  @username_taken
  Scenario: Registration fails when username is already registered
    Given an existing user with username "johndoe"
    And a registration request with:
      | name     | John Clone    |
      | username | johndoe       |
      | email    | john3@example.com |
      | password | secret123     |
    When the user submits the registration request
    Then the system should throw an error "Username already in use"
