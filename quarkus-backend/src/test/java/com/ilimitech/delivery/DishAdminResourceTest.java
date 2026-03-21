package com.ilimitech.delivery;

import com.ilimitech.delivery.config.IntegrationTestResource;
import com.ilimitech.delivery.infrastructure.adapter.in.rest.dto.AdminDishWriteDto;
import com.ilimitech.delivery.support.AdminTestAuth;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(IntegrationTestResource.class)
class DishAdminResourceTest {

    @Test
    void testGetAllDishes() {
        given()
          .header("Authorization", AdminTestAuth.bearerToken())
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
        AdminDishWriteDto dto = new AdminDishWriteDto();
        dto.name = "Test Dish";
        dto.description = "Test Description";
        dto.price = new java.math.BigDecimal("9.99");
        dto.restaurantId = 1L;

        given()
            .header("Authorization", AdminTestAuth.bearerToken())
            .body(dto)
            .contentType("application/json")
            .when().post("/admin/dishes")
            .then()
               .statusCode(404); // restaurant 1 puede no existir en test
    }

    @Test
    void testUpdateDish() {
        AdminDishWriteDto dto = new AdminDishWriteDto();
        dto.name = "Updated Test Dish";
        dto.description = "Updated Description";
        dto.price = new java.math.BigDecimal("14.99");

        given()
            .header("Authorization", AdminTestAuth.bearerToken())
            .body(dto)
            .contentType("application/json")
            .when().put("/admin/dishes/1")
            .then()
               .statusCode(404);
    }

    @Test
    void testDeleteDish() {
        given()
          .header("Authorization", AdminTestAuth.bearerToken())
          .when().delete("/admin/dishes/1")
          .then()
             .statusCode(204); // No content for successful deletion
    }
}