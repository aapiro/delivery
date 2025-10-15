package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class AuthResourceTest {

    @Test
    public void testGetUserProfile() {
        given()
            .when().get("/auth/profile")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("User profile retrieved successfully"))
            .body("user.id", notNullValue())
            .body("user.email", notNullValue());
    }

    @Test
    public void testLogout() {
        given()
            .when().post("/auth/logout")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("User logged out successfully"));
    }
}