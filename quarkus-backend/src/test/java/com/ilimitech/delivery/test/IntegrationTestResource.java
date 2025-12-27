// java
package com.ilimitech.delivery.test;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class IntegrationTestResource implements QuarkusTestResourceLifecycleManager {

    private static PostgreSQLContainer<?> postgres;
    private static final int POSTGRES_INTERNAL_PORT = 5432;

    private static String jdbcUrl;
    private static String username;
    private static String password;
    private static Integer mappedPort;
    private static String host;

    @Override
    public Map<String, String> start() {
        if (postgres == null) {
            // 1. Instanciación básica
            postgres = new PostgreSQLContainer<>(DockerImageName.parse(TestContainerConfig.getPostgresImage())
                    .asCompatibleSubstituteFor(PostgreSQLContainer.NAME))
                    .withDatabaseName(TestContainerConfig.getDatabaseName())
                    .withUsername(TestContainerConfig.getUsername())
                    .withPassword(TestContainerConfig.getPassword());

            // 2. Configuración de rendimiento (Sin el comando 'postgres' inicial, ya que la imagen ya lo tiene como entrypoint)
            postgres.withCommand("-c", "fsync=off",
                    "-c", "synchronous_commit=off",
                    "-c", "max_connections=" + TestContainerConfig.getMaxConnections(),
                    "-c", "shared_buffers=" + TestContainerConfig.getSharedBuffers());

            postgres.withSharedMemorySize(TestContainerConfig.getSharedMemorySize());

            // 3. ESTRATEGIA DE ESPERA: Aumentar el timeout aquí directamente
            // Es mejor esperar a que esté listo para aceptar conexiones que solo el puerto abierto
            postgres.waitingFor(Wait.forListeningPort()
                    .withStartupTimeout(Duration.ofSeconds(TestContainerConfig.getStartupTimeout())));

            // 4. REUSE (Opcional)
            if (TestContainerConfig.isReuseEnabled()) {
                postgres.withReuse(true);
            }

            // 5. IMPORTANTE: NO uses withCreateContainerCmdModifier para bindeos fijos de puertos
            // Si necesitas un puerto específico por alguna razón externa, hazlo así:
            Integer hostPort = TestContainerConfig.getHostPort();
            if (hostPort != null) {
                postgres.withCreateContainerCmdModifier(cmd -> {
                    cmd.withHostConfig(new HostConfig().withPortBindings(
                            new PortBinding(Ports.Binding.bindPort(hostPort), new ExposedPort(POSTGRES_INTERNAL_PORT))
                    ));
                });
            }

            postgres.start();

            // 6. Captura de datos DESPUÉS de start()
            jdbcUrl = postgres.getJdbcUrl();
            username = postgres.getUsername();
            password = postgres.getPassword();
            mappedPort = postgres.getMappedPort(POSTGRES_INTERNAL_PORT);
            host = postgres.getHost();

            logContainerInfo();
        }

        Map<String, String> config = new HashMap<>();
        config.put("quarkus.datasource.jdbc.url", jdbcUrl);
        config.put("quarkus.datasource.username", username);
        config.put("quarkus.datasource.password", password);
        // Si usas Reactive:
        // config.put("quarkus.datasource.reactive.url", jdbcUrl.replace("jdbc:", ""));

        return config;
    }

    @Override
    public void stop() {
        // Con reuse(true), no detenemos el contenedor
    }

    private void logContainerInfo() {
        String separator = "=".repeat(70);
        System.out.println("\n" + separator);
        System.out.println("PostgreSQL TestContainer - Información");
        System.out.println(separator);
        System.out.println("JDBC URL:         " + jdbcUrl);
        System.out.println("Host:             " + host);
        System.out.println("Puerto mapeado:   " + mappedPort);
        System.out.println("Database:         " + postgres.getDatabaseName());
        System.out.println("Username:         " + username);
        System.out.println("Container ID:     " + postgres.getContainerId().substring(0, 12));
        System.out.println(separator + "\n");
    }

    public static String getJdbcUrl() {
        return jdbcUrl;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static Integer getMappedPort() {
        return mappedPort;
    }

    public static String getHost() {
        return host;
    }

    public static PostgreSQLContainer<?> getContainer() {
        return postgres;
    }

    public static boolean isContainerRunning() {
        return postgres != null && postgres.isRunning();
    }
}
