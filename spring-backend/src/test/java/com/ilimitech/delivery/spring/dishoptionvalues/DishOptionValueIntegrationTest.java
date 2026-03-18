package com.ilimitech.delivery.spring.dishoptionvalues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.dishoptionvalues.dto.CreateDishOptionValueDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

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
public class DishOptionValueIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void flow_shouldCreateListAndDelete_andRejectInvalidRequests() throws Exception {

        // POST válido
        CreateDishOptionValueDto dto = new CreateDishOptionValueDto();
        dto.setOptionId(1L);
        dto.setName("Grande");
        dto.setExtraPrice(new BigDecimal("1.50"));

        String created = mockMvc.perform(post("/dish-option-values")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.optionId", is(1)))
                .andExpect(jsonPath("$.name", is("Grande")))
                .andReturn().getResponse().getContentAsString();

        // GET lista con 1 elemento
        mockMvc.perform(get("/dish-option-values").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // DELETE
        Long id = objectMapper.readTree(created).get("id").asLong();
        mockMvc.perform(delete("/dish-option-values/" + id))
                .andExpect(status().isNoContent());

        // GET lista vacía
        mockMvc.perform(get("/dish-option-values").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // Negativo: optionId null -> 400
        CreateDishOptionValueDto bad1 = new CreateDishOptionValueDto();
        bad1.setName("Pequeño");
        mockMvc.perform(post("/dish-option-values")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad1)))
                .andExpect(status().isBadRequest());

        // Negativo: name blank -> 400
        CreateDishOptionValueDto bad2 = new CreateDishOptionValueDto();
        bad2.setOptionId(1L);
        bad2.setName("  ");
        mockMvc.perform(post("/dish-option-values")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad2)))
                .andExpect(status().isBadRequest());

        // Negativo: extraPrice negativo -> 400
        CreateDishOptionValueDto bad3 = new CreateDishOptionValueDto();
        bad3.setOptionId(1L);
        bad3.setName("Mediano");
        bad3.setExtraPrice(new BigDecimal("-0.50"));
        mockMvc.perform(post("/dish-option-values")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad3)))
                .andExpect(status().isBadRequest());
    }
}

