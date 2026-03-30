Feature: TC4 - Cross-Endpoint Data Consistency
  As a tester, I want to verify that a pet created with a specific category
  appears in the correct inventory bucket after its status is updated.

  Scenario Outline: Pet created as available appears in sold list after update
    Given I create a pet with name "<petName>" category "<categoryName>" and status "<initialStatus>"
    Then the response status code should be 200
    And I extract and store the pet ID from the response

    When I update the pet's status to "<updatedStatus>"
    Then the response status code should be 200

    When I fetch the store inventory
    Then the response status code should be 200

    When I fetch pets by status "<updatedStatus>"
    Then the response status code should be 200
    And the stored pet ID should be present in the list of "<updatedStatus>" pets

    Examples:
      | petName           | categoryName      | initialStatus | updatedStatus |
      | HighValueBulldog  | HighValueBulldog  | available     | sold          |
