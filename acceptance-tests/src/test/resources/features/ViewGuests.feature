#As an organizer user, I want to check the detailed information of confirmed guests 
#so that I can copy their contact information.
#Acceptance criteria: Given the user is on the main page, When they click on an event, 
#Then the system redirects them to a new page with the personal information of the confirmed guests.

Feature: View confirmed guests of an event

  Background:
    Given the organizer is authenticated with user id 1

  Scenario: Organizer views the confirmed guests of an event
    Given an event is created with name "Party" and id 10 and has confirmed guests
    When the organizer requests the confirmed guests of event 10
    Then the system should return status 200
    And the response should contain a list of confirmed guests

  Scenario: Organizer views an event with no confirmed guests
    Given an event is created with name "Meeting" and id 11 and has no confirmed guests
    When the organizer requests the confirmed guests of event 11
    Then the system should return status 200
    And the response should contain an empty guest list

