package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class AllNewEndpointsTest {

    // Test all new endpoints that were implemented

    @Test
    public void testAllDishEndpoints() {
        // Search dishes endpoint
        given()
            .when().get("/dishes/search?query=pizza")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        // Restaurant dish categories endpoint  
        given()
            .when().get("/restaurants/1/dish-categories")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    public void testAllCartEndpoints() {
        // Get cart
        given()
            .when().get("/cart")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        // Add to cart
        given()
            .contentType(ContentType.JSON)
            .body("{ \"id\": 1, \"name\": \"Test Item\", \"quantity\": 1, \"price\": 10.99 }")
            .when().post("/cart/add")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        // Update cart item
        given()
            .contentType(ContentType.JSON)
            .body("{ \"id\": 1, \"quantity\": 2 }")
            .when().put("/cart/update")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        // Remove from cart
        given()
            .when().delete("/cart/remove/1")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        // Clear cart
        given()
            .when().delete("/cart/clear")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    public void testAllUserAddressEndpoints() {
        // Get addresses
        given()
            .when().get("/users/addresses")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        // Add address
        given()
            .contentType(ContentType.JSON)
            .body("{ \"street\": \"Calle Principal 123\", \"city\": \"Madrid\", \"postalCode\": \"28001\" }")
            .when().post("/users/addresses")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        // Update address
        given()
            .contentType(ContentType.JSON)
            .body("{ \"id\": 1, \"street\": \"Calle Actualizada\" }")
            .when().put("/users/addresses/1")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        // Delete address
        given()
            .when().delete("/users/addresses/1")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        // Set default address
        given()
            .when().put("/users/addresses/1/default")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    public void testAllPaymentEndpoints() {
        // Get payment methods
        given()
            .when().get("/payments/methods")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        // Process payment
        given()
            .contentType(ContentType.JSON)
            .body("{ \"amount\": 100.0, \"method\": \"credit_card\", \"orderId\": \"order_123\" }")
            .when().post("/payments/process")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        // Handle webhook
        given()
            .contentType(ContentType.JSON)
            .body("{ \"event\": \"payment.completed\", \"data\": { \"transactionId\": \"txn_456\" } }")
            .when().post("/payments/webhook")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    public void testAuthEndpoints() {
        // Get profile
        given()
            .when().get("/auth/profile")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);

        // Logout
        given()
            .when().post("/auth/logout")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }
}