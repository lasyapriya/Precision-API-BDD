package com.veeva.petstore.stepdefinitions;

import com.veeva.petstore.clients.StoreClient;
import com.veeva.petstore.context.ScenarioContext;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StoreSteps {

    private static final Logger log = LogManager.getLogger(StoreSteps.class);

    private final StoreClient storeClient = new StoreClient();
    private final ScenarioContext context;
    private Response lastResponse;

    public StoreSteps(ScenarioContext context) {
        this.context = context;
    }

    @Given("I fetch the store inventory")
    @When("I fetch the store inventory")
    public void fetchInventory() {
        lastResponse = storeClient.getInventory();
        context.set("inventoryResponse", lastResponse);
        log.info("Inventory response: {}", lastResponse.asString());
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expected) {
        // Delegate via context — PetSteps owns the "last response" pattern
        // This step is intentionally a pass-through for store-specific scenarios
        assertEquals(expected, lastResponse.statusCode(),
                "Store endpoint returned HTTP " + lastResponse.statusCode());
    }

    @And("I extract the count of {string} pets from the inventory")
    public void extractInventoryCount(String status) {
        Map<String, Object> inventory = lastResponse.jsonPath().getMap("$");
        Object rawCount = inventory.get(status);
        int count = rawCount != null ? ((Number) rawCount).intValue() : 0;
        context.set("inventoryAvailableCount", count);
        log.info("Inventory count for '{}': {}", status, count);
    }
}
