package com.ilimitech.delivery;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class PaymentResourceTest {

    @Test
    public void testGetPaymentMethods() {
        given()
            .when().get("/payments/methods")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Payment methods retrieved successfully"))
            .body("methods", notNullValue());
    }

    @Test
    public void testProcessPayment() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"amount\": 100.0, \"method\": \"credit_card\", \"orderId\": \"order_123\" }")
            .when().post("/payments/process")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Payment processed successfully"))
            .body("transactionId", notNullValue());
    }

    @Test
    public void testHandleWebhook() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"event\": \"payment.completed\", \"data\": { \"transactionId\": \"txn_456\" } }")
            .when().post("/payments/webhook")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Webhook processed successfully"));
    }
}