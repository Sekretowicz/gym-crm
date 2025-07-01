Feature: Trainee management

  Scenario: Successfully create a new trainee
    Given I prepare valid trainee data
    When I send POST request to /trainees
    Then the response status should be 200