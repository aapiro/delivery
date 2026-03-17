package com.ilimitech.delivery.spring.dishes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.dishes.dto.CreateDishDto;
import com.ilimitech.delivery.spring.dishes.dto.UpdateDishDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
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
public class DishIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudFlow_shouldWork_andValidationShouldRejectBadRequests() throws Exception {
        CreateDishDto create = new CreateDishDto();
        create.setRestaurantId(1L);
        create.setName("Taco");
        create.setPrice(new java.math.BigDecimal("5.50"));

        String createdJson = mockMvc.perform(post("/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/dishes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Taco")));

        var node = objectMapper.readTree(createdJson);
        Long id = node.get("id").asLong();

        mockMvc.perform(get("/dishes/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())));

        UpdateDishDto update = new UpdateDishDto();
        update.setName("Taco Updated");

        mockMvc.perform(put("/dishes/" + id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Taco Updated")));

        mockMvc.perform(delete("/dishes/" + id)).andExpect(status().isNoContent());

        mockMvc.perform(get("/dishes").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));

        // Negative cases: missing name
        CreateDishDto bad = new CreateDishDto();
        bad.setRestaurantId(1L);
        bad.setPrice(new java.math.BigDecimal("2.00"));

        mockMvc.perform(post("/dishes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());

        // Negative: price null
        CreateDishDto bad2 = new CreateDishDto();
        bad2.setRestaurantId(1L);
        bad2.setName("NoPrice");

        mockMvc.perform(post("/dishes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bad2)))
                .andExpect(status().isBadRequest());
    }
}

