Feature: TC2 - Inventory Analysis (Complex Data Parsing)
  As a tester, I want to verify that the inventory count for "available"
  pets matches the actual list returned by findByStatus.

  Scenario: Inventory count matches findByStatus count
    Given I fetch the store inventory
    Then the response status code should be 200
    And I extract the count of "available" pets from the inventory

    When I fetch pets by status "available"
    Then the response status code should be 200
    And the count of pets in the response should match the inventory available count
