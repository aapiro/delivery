package com.ilimitech.delivery;

import com.ilimitech.delivery.config.IntegrationTestResource;
import com.ilimitech.delivery.support.AdminTestAuth;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@QuarkusTestResource(IntegrationTestResource.class)
class AdminAuthResourceTest {

    @Test
    void loginOk() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + AdminTestAuth.EMAIL + "\",\"password\":\"" + AdminTestAuth.PASSWORD + "\"}")
                .when()
                .post("/admin/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("refreshToken", notNullValue())
                .body("admin.email", equalTo(AdminTestAuth.EMAIL))
                .body("admin.role", equalTo("SUPER_ADMIN"))
                .body("expiresIn", greaterThan(0));
    }

    @Test
    void loginWrongPassword() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + AdminTestAuth.EMAIL + "\",\"password\":\"wrong\"}")
                .when()
                .post("/admin/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    void adminDishesRequiresAuth() {
        given()
                .when()
                .get("/admin/dishes")
                .then()
                .statusCode(401);
    }

    @Test
    void refreshWithValidToken() {
        String refresh = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + AdminTestAuth.EMAIL + "\",\"password\":\"" + AdminTestAuth.PASSWORD + "\"}")
                .when()
                .post("/admin/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("refreshToken");

        given()
                .contentType(ContentType.JSON)
                .body("{\"refreshToken\":\"" + refresh + "\"}")
                .when()
                .post("/admin/auth/refresh")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("admin.email", equalTo(AdminTestAuth.EMAIL));
    }

    @Test
    void getProfileWithBearer() {
        given()
                .header("Authorization", AdminTestAuth.bearerToken())
                .when()
                .get("/admin/auth")
                .then()
                .statusCode(200)
                .body("email", equalTo(AdminTestAuth.EMAIL))
                .body("role", equalTo("SUPER_ADMIN"));
    }
}
