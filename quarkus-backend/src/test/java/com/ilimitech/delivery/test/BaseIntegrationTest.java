package com.ilimitech.delivery.test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;

import javax.sql.DataSource;

/**
 * Clase base para tests de integración con PostgreSQL y Liquibase.
 * Configura automáticamente TestContainers y proporciona utilidades comunes.
 */
@QuarkusTest
@QuarkusTestResource(IntegrationTestResource.class)
public abstract class BaseIntegrationTest {

    @Inject
    protected EntityManager em;

    @Inject
    protected DataSource dataSource;

    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    protected String jdbcUrl;

    @ConfigProperty(name = "quarkus.datasource.username")
    protected String username;

    @BeforeEach
    public void setupBase() {
        // Puedes agregar configuración común aquí si es necesario
    }

    /**
     * Ejecuta una query y retorna el resultado
     */
    @Transactional
    protected <T> T executeQuery(String jpql, Class<T> resultClass) {
        return em.createQuery(jpql, resultClass).getSingleResult();
    }

    /**
     * Cuenta registros de una entidad
     */
    @Transactional
    protected Long countEntities(String entityName) {
        String query = String.format("SELECT COUNT(e) FROM %s e", entityName);
        return em.createQuery(query, Long.class).getSingleResult();
    }

    /**
     * Persiste y hace flush de una entidad
     */
    @Transactional
    protected <T> T persistAndFlush(T entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    /**
     * Limpia el contexto de persistencia
     */
    protected void clearEntityManager() {
        em.clear();
    }

    /**
     * Ejecuta SQL nativo
     */
    @Transactional
    protected int executeNativeUpdate(String sql) {
        return em.createNativeQuery(sql).executeUpdate();
    }

    /**
     * Verifica si existe un registro con el id dado
     */
    @Transactional
    protected <T> boolean existsById(Class<T> entityClass, Object id) {
        return em.find(entityClass, id) != null;
    }
}