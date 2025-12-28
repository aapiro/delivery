package com.ilimitech.delivery;

import com.ilimitech.delivery.infrastructure.adapter.out.persistence.DishEntity;
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
        DishEntity dishEntity = new DishEntity();
        dishEntity.setName("Test Dish");
        dishEntity.setDescription("Test Description");
        dishEntity.setPrice(new java.math.BigDecimal("9.99"));
        
        given()
            .body(dishEntity)
            .contentType("application/json")
            .when().post("/admin/dishes")
            .then()
               .statusCode(200);
    }

    @Test
    void testUpdateDish() {
        DishEntity dishEntity = new DishEntity();
        dishEntity.setName("Updated Test Dish");
        dishEntity.setDescription("Updated Description");
        dishEntity.setPrice(new java.math.BigDecimal("14.99"));
        
        given()
            .body(dishEntity)
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