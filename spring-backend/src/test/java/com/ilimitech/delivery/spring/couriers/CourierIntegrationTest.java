package com.ilimitech.delivery.spring.couriers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.couriers.dto.CreateCourierDto;
import com.ilimitech.delivery.spring.couriers.dto.UpdateCourierDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "server.servlet.context-path=/api"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CourierIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void crudFlow_shouldWork_andValidationShouldRejectBadRequests() throws Exception {

        // POST - crear courier válido
        CreateCourierDto create = new CreateCourierDto();
        create.setName("Carlos López");
        create.setVehicleType("MOTO");
        create.setIsOnline(true);
        create.setIsActive(true);

        String createdJson = mockMvc.perform(post("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name", is("Carlos López")))
                .andExpect(jsonPath("$.vehicleType", is("MOTO")))
                .andReturn().getResponse().getContentAsString();

        // GET - listar
        mockMvc.perform(get("/couriers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        var node = objectMapper.readTree(createdJson);
        Long id = node.get("id").asLong();

        // GET by id
        mockMvc.perform(get("/couriers/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())));

        // PUT - actualizar nombre
        UpdateCourierDto update = new UpdateCourierDto();
        update.setName("Carlos Gómez");
        mockMvc.perform(put("/couriers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Carlos Gómez")));

        // DELETE
        mockMvc.perform(delete("/couriers/" + id))
                .andExpect(status().isNoContent());

        // GET - lista vacía
        mockMvc.perform(get("/couriers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // GET by id inexistente → 404
        mockMvc.perform(get("/couriers/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Negativo: name null → 400
        CreateCourierDto bad = new CreateCourierDto();
        mockMvc.perform(post("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());

        // Negativo: name blank → 400
        CreateCourierDto bad2 = new CreateCourierDto();
        bad2.setName("   ");
        mockMvc.perform(post("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad2)))
                .andExpect(status().isBadRequest());
    }
}

