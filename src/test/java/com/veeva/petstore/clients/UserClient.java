package com.veeva.petstore.clients;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final Logger log = LogManager.getLogger(UserClient.class);
    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    private RequestSpecification baseSpec() {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .log().ifValidationFails();
    }

    /** POST /user */
    public Response createUser(Map<String, Object> body) {
        log.info("Creating user: {}", body.get("username"));
        return baseSpec().body(body).when().post("/user");
    }

    /** GET /user/{username} */
    public Response getUserByUsername(String username) {
        log.info("Fetching user: {}", username);
        return baseSpec().pathParam("username", username).when().get("/user/{username}");
    }

    /** GET /user/login?username=x&password=y */
    public Response loginUser(String username, String password) {
        log.info("Logging in user: {}", username);
        return baseSpec()
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get("/user/login");
    }
}
