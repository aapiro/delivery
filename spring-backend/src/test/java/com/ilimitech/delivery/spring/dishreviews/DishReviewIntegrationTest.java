package com.ilimitech.delivery.spring.dishreviews;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.dishreviews.dto.CreateDishReviewDto;
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
public class DishReviewIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void flow_shouldCreateAndList_andRejectInvalidRequests() throws Exception {

        // POST válido
        CreateDishReviewDto dto = new CreateDishReviewDto();
        dto.setDishId(1L);
        dto.setUserId(2L);
        dto.setRating(4);
        dto.setComment("Muy bueno");
        dto.setTags("delicious,fresh");

        mockMvc.perform(post("/dish-reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.dishId", is(1)))
                .andExpect(jsonPath("$.rating", is(4)));

        // GET lista con 1 elemento
        mockMvc.perform(get("/dish-reviews").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // Negativo: dishId null -> 400
        CreateDishReviewDto bad1 = new CreateDishReviewDto();
        bad1.setUserId(2L);
        bad1.setRating(3);
        mockMvc.perform(post("/dish-reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad1)))
                .andExpect(status().isBadRequest());

        // Negativo: rating null -> 400
        CreateDishReviewDto bad2 = new CreateDishReviewDto();
        bad2.setDishId(1L);
        bad2.setUserId(2L);
        mockMvc.perform(post("/dish-reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad2)))
                .andExpect(status().isBadRequest());

        // Negativo: rating = 6 (fuera de rango) -> 400
        CreateDishReviewDto bad3 = new CreateDishReviewDto();
        bad3.setDishId(1L);
        bad3.setUserId(2L);
        bad3.setRating(6);
        mockMvc.perform(post("/dish-reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad3)))
                .andExpect(status().isBadRequest());
    }
}

