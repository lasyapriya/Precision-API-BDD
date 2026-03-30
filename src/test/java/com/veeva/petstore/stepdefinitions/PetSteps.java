package com.veeva.petstore.stepdefinitions;

import com.veeva.petstore.clients.PetClient;
import com.veeva.petstore.clients.StoreClient;
import com.veeva.petstore.context.ScenarioContext;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PetSteps {

    private static final Logger log = LogManager.getLogger(PetSteps.class);

    private final PetClient petClient = new PetClient();
    private final StoreClient storeClient = new StoreClient();
    private final ScenarioContext context;

    private Response lastResponse;

    // Cucumber uses dependency injection - ScenarioContext is shared
    public PetSteps(ScenarioContext context) {
        this.context = context;
    }

    // ==================== TC1 Steps ====================

    @Given("I create a pet with name {string} and status {string}")
    public void createPet(String name, String status) {
        long uniqueId = System.currentTimeMillis() % 1_000_000;
        Map<String, Object> body = new HashMap<>();
        body.put("id", uniqueId);
        body.put("name", name);
        body.put("status", status);
        lastResponse = petClient.createPet(body);
        context.set("petBody", body);
        log.info("Create pet response: {}", lastResponse.asString());
    }

    @Given("I create a pet with name {string} category {string} and status {string}")
    public void createPetWithCategory(String name, String category, String status) {
        long uniqueId = System.currentTimeMillis() % 1_000_000;
        Map<String, Object> categoryMap = new HashMap<>();
        categoryMap.put("id", 1);
        categoryMap.put("name", category);

        Map<String, Object> body = new HashMap<>();
        body.put("id", uniqueId);
        body.put("name", name);
        body.put("status", status);
        body.put("category", categoryMap);
        lastResponse = petClient.createPet(body);
        context.set("petBody", body);
        log.info("Create pet with category response: {}", lastResponse.asString());
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedCode) {
        int actual = lastResponse.statusCode();
        log.info("Expected status: {}, Actual status: {}", expectedCode, actual);
        assertEquals(expectedCode, actual,
                "Expected HTTP " + expectedCode + " but got " + actual +
                ". Response: " + lastResponse.asString());
    }

    @And("I extract and store the pet ID from the response")
    public void extractAndStorePetId() {
        long petId = lastResponse.jsonPath().getLong("id");
        context.set("petId", petId);
        log.info("Stored pet ID: {}", petId);
    }

    @When("I retrieve the pet by the stored ID")
    public void retrievePetByStoredId() {
        long petId = context.getLong("petId");
        lastResponse = petClient.getPetById(petId);
        log.info("Get pet response: {}", lastResponse.asString());
    }

    @Then("the pet name in the response should be {string}")
    public void verifyPetName(String expectedName) {
        String actualName = lastResponse.jsonPath().getString("name");
        assertEquals(expectedName, actualName,
                "Expected name '" + expectedName + "' but got '" + actualName + "'");
    }

    @Then("the pet status in the response should be {string}")
    public void verifyPetStatus(String expectedStatus) {
        String actualStatus = lastResponse.jsonPath().getString("status");
        assertEquals(expectedStatus, actualStatus,
                "Expected status '" + expectedStatus + "' but got '" + actualStatus + "'");
    }

    @When("I update the pet's status to {string}")
    public void updatePetStatus(String newStatus) {
        long petId = context.getLong("petId");
        Map<String, Object> body = new HashMap<>();
        body.put("id", petId);
        body.put("status", newStatus);
        // Re-use existing name if stored
        Map<String, Object> originalBody = (Map<String, Object>) context.get("petBody");
        if (originalBody != null) body.put("name", originalBody.get("name"));
        lastResponse = petClient.updatePet(body);
        log.info("Update pet response: {}", lastResponse.asString());
    }

    @When("I delete the pet using the stored ID")
    public void deletePetByStoredId() {
        long petId = context.getLong("petId");
        lastResponse = petClient.deletePet(petId);
        log.info("Delete pet response: {}", lastResponse.asString());
    }

    // ==================== TC2 Steps ====================

    @When("I fetch pets by status {string}")
    public void fetchPetsByStatus(String status) {
        lastResponse = petClient.findByStatus(status);
        log.info("Find by status response size: {} bytes", lastResponse.asString().length());
    }

    @Then("the count of pets in the response should match the inventory available count")
    public void verifyCountMatchesInventory() {
        int inventoryCount = (Integer) context.get("inventoryAvailableCount");
        List<Object> petList = lastResponse.jsonPath().getList("$");
        int findByStatusCount = petList.size();
        log.info("Inventory count: {}, findByStatus count: {}", inventoryCount, findByStatusCount);
        // Allow small delta due to live API state changes between calls
        assertTrue(Math.abs(inventoryCount - findByStatusCount) <= 10,
                "Inventory available count (" + inventoryCount +
                ") does not match findByStatus count (" + findByStatusCount +
                "). Acceptable delta is 10 due to live API fluctuation.");
    }

    // ==================== TC4 Steps ====================

    @Then("the stored pet ID should be present in the list of {string} pets")
    public void verifyPetInStatusList(String status) {
        long petId = context.getLong("petId");
        List<Map<String, Object>> pets = lastResponse.jsonPath().getList("$");

        boolean found = pets.stream()
                .anyMatch(pet -> {
                    Object idObj = pet.get("id");
                    long id = idObj instanceof Integer ? ((Integer) idObj).longValue() : (Long) idObj;
                    return id == petId;
                });

        log.info("Searching for pet ID {} in {} {} pets. Found: {}", petId, pets.size(), status, found);
        assertTrue(found, "Pet with ID " + petId + " was NOT found in the list of '" + status + "' pets");
    }
}
