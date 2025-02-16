Feature: Vueling flight search

  Narrative:
  In order to find and book flights
  As a traveler using the Vueling website
  I want to be able to search for flights under specific conditions

  Scenario: Search for one-way flights
    Given I'm on the Vueling flight search page
    When I enter the following flight search details:
      | origin  | destination | departureDate | tripType | passengers |
      | Madrid  | Barcelona   | 2025-06-01    | One-way  | 1          |
    Then I should see available flights in the results list
