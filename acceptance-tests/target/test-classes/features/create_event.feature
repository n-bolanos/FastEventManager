#As an organizer user, I want to create a new event 
#so that I can invite people and manage attendance to that event.
#Acceptance criteria: Given the user is on the main page, 
#When they click on the “Create event” button and fill the form, 
#Then the system saves this information in the database and updates the event list.

Feature: Create event
  As an organizer user
  I want to create a new event
  So that I can invite people and manage attendance for that event.

  @create_event_success
  Scenario: Successful event creation
    Given the organizer is authenticated
    And an event creation request with:
      | name        | Birthday Party      |
      | description | Celebration event   |
      | date        | 2025-10-12          |
      | location    | New York            |
    When the organizer submits the event creation request
    Then the system should save the event in the database
    And the response should contain message "Event created successfully"

  @create_event_missing_fields
  Scenario: Event creation fails when fields are missing
    Given the organizer is authenticated
    And an event creation request with:
      | name        |  |
      | description | Meeting |
      | date        | 2025-10-12 |
      | location    | Office |
    When the organizer submits the event creation request
    Then the response status should be 400
    And the response should contain message "Invalid event data"
