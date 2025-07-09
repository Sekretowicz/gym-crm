Feature: Training Arrangement

  Scenario: Arranging a training session
    Given Test data is prepared
    When I arrange a training session
    Then Message appears in ActiveMQ