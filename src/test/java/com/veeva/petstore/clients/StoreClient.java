package com.veeva.petstore.clients;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class StoreClient {

    private static final Logger log = LogManager.getLogger(StoreClient.class);
    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    private RequestSpecification baseSpec() {
        return given()
                .baseUri(BASE_URL)
                .header("Accept", "application/json")
                .log().ifValidationFails();
    }

    /**
     * GET /store/inventory — returns a map of status -> count
     * e.g. {"available": 240, "sold": 18, "pending": 5}
     */
    public Response getInventory() {
        log.info("Fetching store inventory");
        return baseSpec().when().get("/store/inventory");
    }
}
