package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class AdminDashboardResourceTest {

    @Test
    void testGetDashboardOverview() {
        given()
          .when().get("/admin/dashboard")
          .then()
             .statusCode(200)
             .body("message", is("Admin Dashboard Overview"))
             .body("totalRestaurants", is(0))
             .body("activeRestaurants", is(0))
             .body("totalDishes", is(0))
             .body("availableDishes", is(0))
             .body("totalOrders", is(0))
             .body("recentOrders", is(0));
    }

    @Test
    void testGetDashboardStats() {
        given()
          .when().get("/admin/dashboard/stats")
          .then()
             .statusCode(200)
             .body("totalRestaurants", is(0))
             .body("activeRestaurants", is(0))
             .body("totalDishes", is(0))
             .body("availableDishes", is(0))
             .body("totalOrders", is(0))
             .body("recentOrders", is(0));
    }
}