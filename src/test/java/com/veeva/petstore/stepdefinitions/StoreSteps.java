package com.veeva.petstore.stepdefinitions;

import com.veeva.petstore.clients.StoreClient;
import com.veeva.petstore.context.ScenarioContext;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import java.util.Map;

public class StoreSteps {

    private final StoreClient storeClient = new StoreClient();
    private static final ScenarioContext context = new ScenarioContext();

    private Response lastResponse;

    public StoreSteps() {}

    @Given("I fetch the store inventory")
    public void fetchInventory() {
        lastResponse = storeClient.getInventory();

        // retry once if swagger fails
        if (lastResponse.statusCode() == 404) {
            lastResponse = storeClient.getInventory();
        }

        context.set("lastResponse", lastResponse);
    }

    @And("I extract the count of {string} pets from the inventory")
    public void extractCount(String status) {
        Map<String, Object> map = lastResponse.jsonPath().getMap("$");

        int count = map.get(status) == null ? 0 :
                ((Number) map.get(status)).intValue();

        context.set("inventoryCount", count);
    }
}