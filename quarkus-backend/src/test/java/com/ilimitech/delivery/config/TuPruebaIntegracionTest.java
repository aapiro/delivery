package com.ilimitech.delivery.config;

import com.ilimitech.delivery.infrastructure.adapter.out.persistence.Usuario;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import jakarta.persistence.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(IntegrationTestResource.class)
public class TuPruebaIntegracionTest {

    @Inject
    EntityManager em;

    @Test
    @Transactional
    public void testDatabaseConnection() {
        // Verificar que Liquibase corrió correctamente
        Long count = (Long) em.createQuery("SELECT COUNT(u) FROM Usuario u")
                .getSingleResult();

        assertTrue(count > 0, "Debería haber al menos un usuario");
    }

    @Test
    @Transactional
    public void testInsertarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Nuevo Usuario");
        usuario.setEmail("nuevo@example.com");

        em.persist(usuario);
        em.flush();

        assertNotNull(usuario.getId());
    }
}