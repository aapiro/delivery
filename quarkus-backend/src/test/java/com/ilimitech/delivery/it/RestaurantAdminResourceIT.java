package com.ilimitech.delivery.it;

import com.ilimitech.delivery.config.IntegrationTestResource;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.RestaurantEntity;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@QuarkusTestResource(IntegrationTestResource.class)
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
        System.out.println("Restaurant created successfully");
    }

    @Test
    public void testUpdateRestaurantJson() {
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
                .when().delete("/admin/restaurants/2")
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

    @Test
    void testGetRestaurantByIdNotFound() {
        given()
                .when().get("/admin/restaurants/999")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND); // Assuming no restaurant with ID 1 exists yet
    }

    @Test
    void testCreateRestaurant() {
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setName("Test Restaurant");
        restaurantEntity.setCuisines(Set.of());

        given()
                .body(restaurantEntity)
                .contentType("application/json")
                .when().post("/admin/restaurants")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    void testUpdateRestaurantObject() {
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setName("Updated Test Restaurant");
        restaurantEntity.setCuisines(Set.of());

        given()
                .body(restaurantEntity)
                .contentType("application/json")
                .when().put("/admin/restaurants/999")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

}
