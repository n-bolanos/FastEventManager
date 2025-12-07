#As an organizer user, I want to copy the link associated with any event 
#so that I can share it.
#Acceptance criteria: Given the user is on the main page, 
#When they click on the “Share event” button, 
#Then the system copies the associated link to the clipboard.

Feature: Share event link
  As an organizer user,
  I want to copy the link associated with an event
  so that I can share it with other people.

  Scenario: Organizer copies the event share link
    Given the organizer is authenticated
    And an event exists with id "20" and share link "http://localhost:8003/events/20"
    When the organizer clicks the Share event button for id "20"
    Then the system should copy "http://localhost:8003/events/20" to the clipboard
