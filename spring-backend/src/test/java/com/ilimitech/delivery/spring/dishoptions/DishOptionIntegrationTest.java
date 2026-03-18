package com.ilimitech.delivery.spring.dishoptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.dishoptions.dto.CreateDishOptionDto;
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
public class DishOptionIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void flow_shouldCreateListAndDelete_andRejectInvalidRequests() throws Exception {

        // POST válido
        CreateDishOptionDto dto = new CreateDishOptionDto();
        dto.setDishId(1L);
        dto.setName("Tamaño");
        dto.setRequired(true);

        String created = mockMvc.perform(post("/dish-options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.dishId", is(1)))
                .andExpect(jsonPath("$.name", is("Tamaño")))
                .andExpect(jsonPath("$.required", is(true)))
                .andReturn().getResponse().getContentAsString();

        // GET lista con 1 elemento
        mockMvc.perform(get("/dish-options").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // DELETE
        Long id = objectMapper.readTree(created).get("id").asLong();
        mockMvc.perform(delete("/dish-options/" + id))
                .andExpect(status().isNoContent());

        // GET lista vacía
        mockMvc.perform(get("/dish-options").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // Negativo: dishId null -> 400
        CreateDishOptionDto bad1 = new CreateDishOptionDto();
        bad1.setName("Extra");
        mockMvc.perform(post("/dish-options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad1)))
                .andExpect(status().isBadRequest());

        // Negativo: name blank -> 400
        CreateDishOptionDto bad2 = new CreateDishOptionDto();
        bad2.setDishId(1L);
        bad2.setName("  ");
        mockMvc.perform(post("/dish-options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad2)))
                .andExpect(status().isBadRequest());
    }
}

