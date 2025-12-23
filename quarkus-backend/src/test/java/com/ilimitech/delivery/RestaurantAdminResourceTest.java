package com.ilimitech.delivery;

import com.ilimitech.delivery.restaurant.Restaurant;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class RestaurantAdminResourceTest {

    @Test
    void testGetAllRestaurants() {
        given()
          .when().get("/admin/restaurants")
          .then()
             .statusCode(200);
    }

    @Test
    void testGetRestaurantById() {
        given()
          .when().get("/admin/restaurants/1")
          .then()
             .statusCode(404); // Assuming no restaurant with ID 1 exists yet
    }

    @Test
    void testCreateRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setCuisine("Test Cuisine");
        
        given()
            .body(restaurant)
            .contentType("application/json")
            .when().post("/admin/restaurants")
            .then()
               .statusCode(200);
    }

    @Test
    void testUpdateRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Updated Test Restaurant");
        restaurant.setCuisine("Updated Test Cuisine");
        
        given()
            .body(restaurant)
            .contentType("application/json")
            .when().put("/admin/restaurants/1")
            .then()
               .statusCode(404); // Assuming no restaurant with ID 1 exists yet
    }

    @Test
    void testDeleteRestaurant() {
        given()
          .when().delete("/admin/restaurants/1")
          .then()
             .statusCode(204); // No content for successful deletion
    }
}