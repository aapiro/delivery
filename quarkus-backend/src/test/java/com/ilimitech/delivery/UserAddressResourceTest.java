package com.ilimitech.delivery;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class UserAddressResourceTest {

    @Test
    public void testGetUserAddresses() {
        given()
            .when().get("/users/addresses")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Addresses retrieved successfully"))
            .body("addresses", notNullValue());
    }

    @Test
    public void testAddUserAddress() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"street\": \"Calle Principal 123\", \"city\": \"Madrid\", \"postalCode\": \"28001\" }")
            .when().post("/users/addresses")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Address added successfully"))
            .body("address.street", equalTo("Calle Principal 123"));
    }

    @Test
    public void testUpdateUserAddress() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"id\": 1, \"street\": \"Calle Actualizada 456\" }")
            .when().put("/users/addresses/1")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Address updated successfully"));
    }

    @Test
    public void testDeleteUserAddress() {
        given()
            .when().delete("/users/addresses/1")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Address deleted successfully"));
    }

    @Test
    public void testSetDefaultAddress() {
        given()
            .when().put("/users/addresses/1/default")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Default address updated successfully"));
    }
}