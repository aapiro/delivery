package com.ilimitech.delivery.spring.dishavailability;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.dishavailability.dto.CreateDishAvailabilityDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
public class DishAvailabilityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudFlow_shouldWork_andValidationShouldRejectBadRequests() throws Exception {

        // POST - create valid entry
        CreateDishAvailabilityDto create = new CreateDishAvailabilityDto();
        create.setDishId(1L);
        create.setDayOfWeek(1); // Monday
        create.setStartTime(LocalTime.of(8, 0));
        create.setEndTime(LocalTime.of(22, 0));

        String createdJson = mockMvc.perform(post("/dish-availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.dishId", is(1)))
                .andExpect(jsonPath("$.dayOfWeek", is(1)))
                .andReturn().getResponse().getContentAsString();

        // GET - list should have 1 element
        mockMvc.perform(get("/dish-availability").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].dishId", is(1)));

        // DELETE
        var node = objectMapper.readTree(createdJson);
        Long id = node.get("id").asLong();

        mockMvc.perform(delete("/dish-availability/" + id))
                .andExpect(status().isNoContent());

        // GET - list should be empty after delete
        mockMvc.perform(get("/dish-availability").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // Negative: dishId null → 400
        CreateDishAvailabilityDto bad1 = new CreateDishAvailabilityDto();
        bad1.setDayOfWeek(1);
        mockMvc.perform(post("/dish-availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad1)))
                .andExpect(status().isBadRequest());

        // Negative: dayOfWeek null → 400
        CreateDishAvailabilityDto bad2 = new CreateDishAvailabilityDto();
        bad2.setDishId(1L);
        mockMvc.perform(post("/dish-availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad2)))
                .andExpect(status().isBadRequest());

        // Negative: dayOfWeek out of range (7) → 400
        CreateDishAvailabilityDto bad3 = new CreateDishAvailabilityDto();
        bad3.setDishId(1L);
        bad3.setDayOfWeek(7);
        mockMvc.perform(post("/dish-availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad3)))
                .andExpect(status().isBadRequest());
    }
}

