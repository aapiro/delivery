package com.ilimitech.delivery;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class AuthResourceTest {

    @Test
    public void testRegister() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                  "name": "Alice",
                  "email": "alice@example.com",
                  "password": "secret123"
                }
                """)
            .when().post("/auth/register")
            .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("user.email", equalTo("alice@example.com"))
            .body("token", notNullValue())
            .body("refreshToken", notNullValue());
    }

    @Test
    public void testLogin() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                  "email": "alice@example.com",
                  "password": "secret123"
                }
                """)
            .when().post("/auth/login")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("user.email", equalTo("alice@example.com"))
            .body("token", notNullValue())
            .body("refreshToken", notNullValue());
    }

    @Test
    public void testRefresh() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                  "refreshToken": "dummy-refresh-token"
                }
                """)
            .when().post("/auth/refresh")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("token", notNullValue())
            .body("refreshToken", notNullValue())
            .body("user.email", equalTo("user@example.com"));
    }

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