package com.ilimitech.delivery.support;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

/**
 * Obtiene JWT de admin en tests (requiere seed {@code 005-data-seed-admin-user.sql}).
 */
public final class AdminTestAuth {

    public static final String EMAIL = "admin@delivery.local";
    public static final String PASSWORD = "admin123";

    private AdminTestAuth() {
    }

    public static String bearerToken() {
        String token = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + EMAIL + "\",\"password\":\"" + PASSWORD + "\"}")
                .when()
                .post("/admin/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
        return "Bearer " + token;
    }
}
