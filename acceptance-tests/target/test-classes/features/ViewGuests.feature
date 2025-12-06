#As an organizer user, I want to check the detailed information of confirmed guests 
#so that I can copy their contact information.
#Acceptance criteria: Given the user is on the main page, When they click on an event, 
#Then the system redirects them to a new page with the personal information of the confirmed guests.

Feature: View confirmed guests of an event
  As an organizer user
  I want to check the detailed information of confirmed guests
  So that I can copy their contact information

  Background:
    Given the organizer is authenticated

  Scenario: Organizer views the confirmed guests of an event
    Given there is an event with id "123" that has confirmed guests
    When the organizer selects the event with id "123"
    Then the system returns the confirmed guests list with their personal information

  Scenario: Organizer views an event with no confirmed guests
    Given there is an event with id "456" that has no confirmed guests
    When the organizer selects the event with id "456"
    Then the system returns an empty confirmed guest list
