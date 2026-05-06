Feature: TC1 - The Lifecycle of a Pet (CRUD and Chaining)
  As a tester, I want to create, read, update and delete a pet
  so that I can verify the full CRUD lifecycle works correctly.

  Scenario Outline: Create, retrieve, update and delete a pet
    Given I create a pet with name "<petName>" and status "<initialStatus>"
    Then the response status code should be 200
    And I extract and store the pet ID from the response

    When I retrieve the pet by the stored ID
    Then the response status code should be 200
    And the pet name in the response should be "<petName>"
    And the pet status in the response should be "<initialStatus>"

    When I update the pet's status to "<updatedStatus>"
    Then the response status code should be 200

    When I delete the pet using the stored ID
    Then the response status code should be 200

    When I retrieve the pet by the stored ID
    Then the response status code should be 404

    Examples:
      | petName       | initialStatus | updatedStatus |
      | BuddyLabrador | available     | sold          |
      | MaxPoodle     | available     | sold          |
