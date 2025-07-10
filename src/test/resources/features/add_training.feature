Feature: Training Management

  Scenario: Arranging a training session
    Given Test data is prepared
    When I arrange a training session
    Then Message appears in ActiveMQ
    And Training session appears in the database

  Scenario: Cancelling a training session
    Given Test data is prepared
    When I arrange a training session
    And I cancel the training session
    Then Message appears in ActiveMQ
