package com.ilimitech.delivery;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class DishResourceTest {

    @Test
    public void testSearchDishes() {
        given()
            .when().get("/dishes/search?query=pizza")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Dishes found successfully"))
            .body("dishes", notNullValue());
    }

    @Test
    public void testGetRestaurantDishCategories() {
        given()
            .when().get("/restaurants/1/dish-categories")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Dish categories retrieved successfully"))
            .body("categories", notNullValue());
    }
}