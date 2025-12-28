package com.ilimitech.delivery.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestContainerConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = TestContainerConfig.class
                .getClassLoader()
                .getResourceAsStream("test-container.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            // Usar valores por defecto si no existe el archivo
            System.out.println("No se encontró test-container.properties, usando valores por defecto");
        }
    }

    public static String getPostgresImage() {
        return properties.getProperty("testcontainer.postgres.image", "postgres:15");
    }

    public static String getDatabaseName() {
        return properties.getProperty("testcontainer.postgres.database", "testdb");
    }

    public static String getUsername() {
        return properties.getProperty("testcontainer.postgres.username", "test");
    }

    public static String getPassword() {
        return properties.getProperty("testcontainer.postgres.password", "test");
    }

    public static Integer getHostPort() {
        String port = properties.getProperty("testcontainer.postgres.host-port");
        return port != null ? Integer.parseInt(port) : null;
    }

    public static boolean isReuseEnabled() {
        return Boolean.parseBoolean(
                properties.getProperty("testcontainer.reuse.enabled", "true")
        );
    }

    public static int getMaxConnections() {
        return Integer.parseInt(
                properties.getProperty("testcontainer.postgres.max-connections", "100")
        );
    }

    public static String getSharedBuffers() {
        return properties.getProperty("testcontainer.postgres.shared-buffers", "256MB");
    }

    public static boolean isLogStatements() {
        return Boolean.parseBoolean(
                properties.getProperty("testcontainer.postgres.log-statements", "false")
        );
    }

    public static long getSharedMemorySize() {
        String size = properties.getProperty("testcontainer.postgres.shared-memory", "512");
        return Long.parseLong(size) * 1024 * 1024; // Convertir MB a bytes
    }

    public static int getStartupTimeout() {
        return Integer.parseInt(
                properties.getProperty("testcontainer.startup-timeout", "60")
        );
    }
}
