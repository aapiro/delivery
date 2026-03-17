package com.ilimitech.delivery.spring.courierreviews;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.courierreviews.dto.CreateCourierReviewDto;
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
public class CourierReviewIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void flow_shouldCreateAndList_andRejectInvalidRequests() throws Exception {

        // POST - review válida
        CreateCourierReviewDto dto = new CreateCourierReviewDto();
        dto.setCourierId(1L);
        dto.setUserId(2L);
        dto.setOrderId(3L);
        dto.setRating(5);
        dto.setComment("Muy rápido");

        mockMvc.perform(post("/courier-reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.courierId", is(1)))
                .andExpect(jsonPath("$.rating", is(5)));

        // GET - lista con 1 elemento
        mockMvc.perform(get("/courier-reviews").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // Negativo: courierId null → 400
        CreateCourierReviewDto bad1 = new CreateCourierReviewDto();
        bad1.setUserId(2L);
        bad1.setRating(4);
        mockMvc.perform(post("/courier-reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad1)))
                .andExpect(status().isBadRequest());

        // Negativo: rating null → 400
        CreateCourierReviewDto bad2 = new CreateCourierReviewDto();
        bad2.setCourierId(1L);
        bad2.setUserId(2L);
        mockMvc.perform(post("/courier-reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad2)))
                .andExpect(status().isBadRequest());

        // Negativo: rating fuera de rango (0) → 400
        CreateCourierReviewDto bad3 = new CreateCourierReviewDto();
        bad3.setCourierId(1L);
        bad3.setUserId(2L);
        bad3.setRating(0);
        mockMvc.perform(post("/courier-reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad3)))
                .andExpect(status().isBadRequest());

        // Negativo: rating fuera de rango (6) → 400
        CreateCourierReviewDto bad4 = new CreateCourierReviewDto();
        bad4.setCourierId(1L);
        bad4.setUserId(2L);
        bad4.setRating(6);
        mockMvc.perform(post("/courier-reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad4)))
                .andExpect(status().isBadRequest());
    }
}

