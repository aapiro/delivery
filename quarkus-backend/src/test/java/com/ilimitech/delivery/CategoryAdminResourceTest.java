package com.ilimitech.delivery;

import com.ilimitech.delivery.config.IntegrationTestResource;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.Category;
import com.ilimitech.delivery.support.AdminTestAuth;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(IntegrationTestResource.class)
class CategoryAdminResourceTest {

    @Test
    void testGetAllCategories() {
        given()
          .header("Authorization", AdminTestAuth.bearerToken())
          .when().get("/admin/categories")
          .then()
             .statusCode(200);
    }

    @Test
    void testGetCategoryById() {
        given()
          .header("Authorization", AdminTestAuth.bearerToken())
          .when().get("/admin/categories/1")
          .then()
             .statusCode(404); // Assuming no category with ID 1 exists yet
    }

    @Test
    void testCreateCategory() {
        Category category = new Category();
        category.setName("Test Category");
        
        given()
            .header("Authorization", AdminTestAuth.bearerToken())
            .body(category)
            .contentType("application/json")
            .when().post("/admin/categories")
            .then()
               .statusCode(200);
    }

    @Test
    void testUpdateCategory() {
        Category category = new Category();
        category.setName("Updated Test Category");
        
        given()
            .header("Authorization", AdminTestAuth.bearerToken())
            .body(category)
            .contentType("application/json")
            .when().put("/admin/categories/1")
            .then()
               .statusCode(404); // Assuming no category with ID 1 exists yet
    }

    @Test
    void testDeleteCategory() {
        given()
          .header("Authorization", AdminTestAuth.bearerToken())
          .when().delete("/admin/categories/1")
          .then()
             .statusCode(204); // No content for successful deletion
    }
}