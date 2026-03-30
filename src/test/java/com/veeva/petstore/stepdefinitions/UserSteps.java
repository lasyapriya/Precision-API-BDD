package com.veeva.petstore.stepdefinitions;

import com.veeva.petstore.clients.UserClient;
import com.veeva.petstore.context.ScenarioContext;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserSteps {

    private static final Logger log = LogManager.getLogger(UserSteps.class);

    private final UserClient userClient = new UserClient();
    private final ScenarioContext context;
    private Response lastResponse;

    public UserSteps(ScenarioContext context) {
        this.context = context;
    }

    @Given("I create a user with username {string} and email {string}")
    public void createUser(String username, String email) {
        Map<String, Object> body = new HashMap<>();
        body.put("id", (int)(System.currentTimeMillis() % 100000));
        body.put("username", username);
        body.put("email", email);
        body.put("firstName", "Test");
        body.put("lastName", "User");
        body.put("password", "password123");
        body.put("phone", "0000000000");
        body.put("userStatus", 1);
        lastResponse = userClient.createUser(body);
        log.info("Create user response: {}", lastResponse.asString());
    }

    @Then("the user creation response code should be {int}")
    public void verifyUserCreationCode(int expected) {
        assertEquals(expected, lastResponse.statusCode(),
                "User creation returned HTTP " + lastResponse.statusCode());
    }

    @When("I fetch the user with username {string}")
    public void fetchUser(String username) {
        lastResponse = userClient.getUserByUsername(username);
        log.info("Fetch user response: {} - {}", lastResponse.statusCode(), lastResponse.asString());
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expected) {
        assertEquals(expected, lastResponse.statusCode(),
                "Expected HTTP " + expected + " but got " + lastResponse.statusCode());
    }

    @And("the response message should contain {string}")
    public void verifyResponseMessage(String expectedMsg) {
        String body = lastResponse.asString();
        assertTrue(body.contains(expectedMsg),
                "Expected response to contain '" + expectedMsg + "' but got: " + body);
    }

    @When("I login with username {string} and password {string}")
    public void loginUser(String username, String password) {
        lastResponse = userClient.loginUser(username, password);
        log.info("Login response: {} - {}", lastResponse.statusCode(), lastResponse.asString());
    }

    @Then("the login should not return a valid session token")
    public void verifyNoValidToken() {
        // A failed login returns 400 or a message containing no token prefix
        boolean isFailedLogin = lastResponse.statusCode() != 200 ||
                !lastResponse.asString().contains("logged in user session:");
        assertTrue(isFailedLogin,
                "Login with wrong credentials should not succeed. Response: " + lastResponse.asString());
    }
}
