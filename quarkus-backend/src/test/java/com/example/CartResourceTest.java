package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class CartResourceTest {

    @Test
    public void testGetCart() {
        given()
            .when().get("/cart")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Cart retrieved successfully"))
            .body("items", notNullValue())
            .body("total", equalTo(0.0f));
    }

    @Test
    public void testAddToCart() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"id\": 1, \"name\": \"Pizza Margherita\", \"quantity\": 1, \"price\": 12.99 }")
            .when().post("/cart/add")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Item added to cart successfully"))
            .body("item.id", equalTo(1))
            .body("item.name", equalTo("Pizza Margherita"));
    }

    @Test
    public void testUpdateCartItem() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"id\": 1, \"quantity\": 2 }")
            .when().put("/cart/update")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Cart updated successfully"));
    }

    @Test
    public void testRemoveFromCart() {
        given()
            .when().delete("/cart/remove/1")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Item removed from cart successfully"));
    }

    @Test
    public void testClearCart() {
        given()
            .when().delete("/cart/clear")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Cart cleared successfully"));
    }
}