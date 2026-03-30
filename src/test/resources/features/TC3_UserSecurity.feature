Feature: TC3 - User Security and Error Handling (Negative Testing)
  As a tester, I want to verify that invalid operations return proper errors.

  Scenario Outline: Create user with invalid email
    Given I create a user with username "<username>" and email "<email>"
    Then the user creation response code should be <expectedCode>

    Examples:
      | username      | email         | expectedCode |
      | testUser001   | invalid_email | 200          |

  Scenario: Fetch a non-existent user returns 404
    When I fetch the user with username "nonExistentUser123xyzabc"
    Then the response status code should be 404
    And the response message should contain "User not found"

  Scenario Outline: Login with incorrect credentials returns no valid token
    When I login with username "<username>" and password "<password>"
    Then the login should not return a valid session token

    Examples:
      | username     | password      |
      | wrongUser    | wrongPassword |
