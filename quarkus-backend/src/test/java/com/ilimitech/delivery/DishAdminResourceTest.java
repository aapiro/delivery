package com.ilimitech.delivery;

import com.ilimitech.delivery.dish.Dish;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class DishAdminResourceTest {

    @Test
    void testGetAllDishes() {
        given()
          .when().get("/admin/dishes")
          .then()
             .statusCode(200);
    }

    @Test
    void testGetDishById() {
        given()
          .when().get("/admin/dishes/1")
          .then()
             .statusCode(404); // Assuming no dish with ID 1 exists yet
    }

    @Test
    void testCreateDish() {
        Dish dish = new Dish();
        dish.setName("Test Dish");
        dish.setDescription("Test Description");
        dish.setPrice(new java.math.BigDecimal("9.99"));
        
        given()
            .body(dish)
            .contentType("application/json")
            .when().post("/admin/dishes")
            .then()
               .statusCode(200);
    }

    @Test
    void testUpdateDish() {
        Dish dish = new Dish();
        dish.setName("Updated Test Dish");
        dish.setDescription("Updated Description");
        dish.setPrice(new java.math.BigDecimal("14.99"));
        
        given()
            .body(dish)
            .contentType("application/json")
            .when().put("/admin/dishes/1")
            .then()
               .statusCode(404); // Assuming no dish with ID 1 exists yet
    }

    @Test
    void testDeleteDish() {
        given()
          .when().delete("/admin/dishes/1")
          .then()
             .statusCode(204); // No content for successful deletion
    }
}