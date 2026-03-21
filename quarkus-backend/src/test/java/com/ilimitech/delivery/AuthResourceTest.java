package com.ilimitech.delivery;

import com.ilimitech.delivery.config.IntegrationTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@QuarkusTestResource(IntegrationTestResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthResourceTest {

    @Test
    @Order(1)
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
    @Order(2)
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
    @Order(3)
    public void testRefresh() {
        String email = "refresh-" + UUID.randomUUID() + "@example.com";
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"R\",\"email\":\"" + email + "\",\"password\":\"secret123\"}")
                .when().post("/auth/register")
                .then()
                .statusCode(201);

        String refreshToken = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\",\"password\":\"secret123\"}")
                .when().post("/auth/login")
                .then()
                .statusCode(200)
                .extract().path("refreshToken");

        given()
                .contentType(ContentType.JSON)
                .body("{\"refreshToken\":\"" + refreshToken + "\"}")
                .when().post("/auth/refresh")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("token", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(email));
    }

    @Test
    @Order(4)
    public void testGetUserProfileRequiresAuthentication() {
        given()
            .when().get("/auth/profile")
            .then()
            .statusCode(401);
    }

    @Test
    @Order(5)
    public void testGetUserProfileWithJwt() {
        String email = "profile-" + UUID.randomUUID() + "@example.com";
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":\"P\",\"email\":\"" + email + "\",\"password\":\"secret123\"}")
                .when().post("/auth/register")
                .then()
                .statusCode(201);

        String token = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\",\"password\":\"secret123\"}")
                .when().post("/auth/login")
                .then()
                .statusCode(200)
                .extract().path("token");

        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/auth/profile")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("user.email", equalTo(email));
    }

    @Test
    @Order(6)
    public void testLogout() {
        given()
            .when().post("/auth/logout")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("User logged out successfully"));
    }
}
