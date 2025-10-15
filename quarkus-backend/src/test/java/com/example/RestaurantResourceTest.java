package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class RestaurantResourceTest {

    @Test
    public void testGetAllRestaurants() {
        given()
            .when().get("/restaurants")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Restaurants retrieved successfully"))
            .body("restaurants", notNullValue());
    }

    @Test
    public void testGetRestaurantCategories() {
        given()
            .when().get("/restaurants/categories")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Restaurant categories retrieved successfully"))
            .body("categories", notNullValue());
    }

    @Test
    public void testSearchRestaurants() {
        given()
            .when().get("/restaurants/search?query=pizza")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Restaurants found successfully"))
            .body("restaurants", notNullValue());
    }

}