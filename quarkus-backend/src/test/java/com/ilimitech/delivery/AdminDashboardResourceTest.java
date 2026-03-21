package com.ilimitech.delivery;

import com.ilimitech.delivery.config.IntegrationTestResource;
import com.ilimitech.delivery.support.AdminTestAuth;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(IntegrationTestResource.class)
class AdminDashboardResourceTest {

    @Test
    void testGetDashboardOverview() {
        given()
          .header("Authorization", AdminTestAuth.bearerToken())
          .when().get("/admin/dashboard")
          .then()
             .statusCode(200)
             .body("message", is("Admin Dashboard Overview"))
             .body("totalRestaurants", greaterThanOrEqualTo(0))
             .body("activeRestaurants", greaterThanOrEqualTo(0))
             .body("totalDishes", greaterThanOrEqualTo(0))
             .body("availableDishes", greaterThanOrEqualTo(0))
             .body("totalOrders", greaterThanOrEqualTo(0))
             .body("recentOrders", greaterThanOrEqualTo(0));
    }

    @Test
    void testGetDashboardStats() {
        given()
          .header("Authorization", AdminTestAuth.bearerToken())
          .when().get("/admin/dashboard/stats")
          .then()
             .statusCode(200)
             .body("totalRestaurants", greaterThanOrEqualTo(0))
             .body("activeRestaurants", greaterThanOrEqualTo(0))
             .body("totalDishes", greaterThanOrEqualTo(0))
             .body("availableDishes", greaterThanOrEqualTo(0))
             .body("totalOrders", greaterThanOrEqualTo(0))
             .body("recentOrders", greaterThanOrEqualTo(0));
    }
}