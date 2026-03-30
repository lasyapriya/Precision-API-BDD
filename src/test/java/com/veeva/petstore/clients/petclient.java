package com.veeva.petstore.clients;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class PetClient {

    private static final Logger log = LogManager.getLogger(PetClient.class);
    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    private RequestSpecification baseSpec() {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .log().ifValidationFails();
    }

    /**
     * POST /pet — creates a new pet
     * body: JSON map e.g. {"id": 12345, "name": "Buddy", "status": "available"}
     */
    public Response createPet(Map<String, Object> body) {
        log.info("Creating pet with name: {}", body.get("name"));
        return baseSpec()
                .body(body)
                .when()
                .post("/pet");
    }

    /**
     * GET /pet/{petId} — fetch a pet by ID
     */
    public Response getPetById(long petId) {
        log.info("Fetching pet with ID: {}", petId);
        return baseSpec()
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}");
    }

    /**
     * PUT /pet — update an existing pet
     */
    public Response updatePet(Map<String, Object> body) {
        log.info("Updating pet with ID: {}", body.get("id"));
        return baseSpec()
                .body(body)
                .when()
                .put("/pet");
    }

    /**
     * DELETE /pet/{petId}
     */
    public Response deletePet(long petId) {
        log.info("Deleting pet with ID: {}", petId);
        return baseSpec()
                .pathParam("petId", petId)
                .when()
                .delete("/pet/{petId}");
    }

    /**
     * GET /pet/findByStatus?status=available
     */
    public Response findByStatus(String status) {
        log.info("Finding pets by status: {}", status);
        return baseSpec()
                .queryParam("status", status)
                .when()
                .get("/pet/findByStatus");
    }
}
