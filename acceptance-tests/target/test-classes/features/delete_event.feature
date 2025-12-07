#As an organizer user, I want to delete an event that I have created 
#so that it does not display any more in my main page.
#Acceptance criteria: Given the user is on the main page, 
#When they click on the “Delete event” button, 
#Then the system deletes the event and the information of their guests after confirming the action with the user.



Feature: Delete event
  As an organizer user,
  I want to delete an event that I have created
  so that it no longer appears on my main page.

  @delete_success
  Scenario: Organizer deletes an existing event
    Given the organizer is authenticated
    And an event exists with id "10"
    When the organizer deletes the event with id "10"
    Then the system should respond with status 200
    And the response should contain message "Event deleted successfully"

  @delete_not_found
  Scenario: Organizer attempts to delete a non-existing event
    Given the organizer is authenticated
    And no event exists with id "99"
    When the organizer deletes the event with id "99"
    Then the system should respond with status 404
    And the response should contain message "Event not found"
