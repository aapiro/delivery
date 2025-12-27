package com.ilimitech.delivery.test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(IntegrationTestResource.class)
public class MiTest extends BaseIntegrationTest {

    @Test
    public void testConexionDirecta() {
        // Ahora funciona sin NullPointerException
        String jdbcUrl = IntegrationTestResource.getJdbcUrl();

        assertNotNull(jdbcUrl, "JDBC URL no debería ser null");
        System.out.println("Conectando a: " + jdbcUrl);

        // Extraer el puerto
        String[] parts = jdbcUrl.split(":");
        String port = parts[parts.length - 1].split("/")[0];
        System.out.println("Puerto: " + port);

        // O usar directamente el método helper
        System.out.println("Puerto (helper): " + IntegrationTestResource.getMappedPort());
        System.out.println("Host: " + IntegrationTestResource.getHost());
        System.out.println("Username: " + IntegrationTestResource.getUsername());
    }

    @Test
    public void testContainerInfo() {
        assertTrue(IntegrationTestResource.isContainerRunning());

        System.out.println("=== Información del Contenedor ===");
        System.out.println("URL: " + IntegrationTestResource.getJdbcUrl());
        System.out.println("Host: " + IntegrationTestResource.getHost());
        System.out.println("Puerto: " + IntegrationTestResource.getMappedPort());
        System.out.println("Usuario: " + IntegrationTestResource.getUsername());
    }
}