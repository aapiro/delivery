package com.ilimitech.delivery;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class RestaurantAdminResourceIT {

    @Test
    public void testGetAllRestaurants() {
        given()
                .when().get("/admin/restaurants")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testGetRestaurantById() {
        given()
                .when().get("/admin/restaurants/1")
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    public void testAddRestaurant() {
        given()
                .contentType("application/json")
                .body("{\"name\":\"Test Restaurant\",\"cuisine\":\"Italian\"}")
                .when().post("/admin/restaurants")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    public void testUpdateRestaurant() {
        given()
                .contentType("application/json")
                .body("{\"name\":\"Updated Restaurant\",\"cuisine\":\"Chinese\"}")
                .when().put("/admin/restaurants/1")
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    public void testDeleteRestaurant() {
        given()
                .when().delete("/admin/restaurants/1")
                .then()
                .statusCode(204);
    }

    @Test
    public void testToggleRestaurantStatus() {
        given()
                .when().patch("/admin/restaurants/1/toggle-status")
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }
}
