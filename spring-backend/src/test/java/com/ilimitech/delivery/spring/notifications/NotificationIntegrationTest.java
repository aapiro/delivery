package com.ilimitech.delivery.spring.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.notifications.dto.CreateNotificationDto;
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
public class NotificationIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void flow_shouldCreateAndList_andRejectInvalidRequests() throws Exception {

        // POST válido
        CreateNotificationDto dto = new CreateNotificationDto();
        dto.setUserId(1L);
        dto.setType("ORDER_CONFIRMED");
        dto.setTitle("Pedido confirmado");
        dto.setMessage("Tu pedido ha sido confirmado");

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.type", is("ORDER_CONFIRMED")));

        // GET lista con 1 elemento
        mockMvc.perform(get("/notifications").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // Negativo: userId null -> 400
        CreateNotificationDto bad1 = new CreateNotificationDto();
        bad1.setType("ORDER_CONFIRMED");
        bad1.setTitle("Test");
        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad1)))
                .andExpect(status().isBadRequest());

        // Negativo: type blank -> 400
        CreateNotificationDto bad2 = new CreateNotificationDto();
        bad2.setUserId(1L);
        bad2.setType("  ");
        bad2.setTitle("Test");
        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad2)))
                .andExpect(status().isBadRequest());

        // Negativo: title blank -> 400
        CreateNotificationDto bad3 = new CreateNotificationDto();
        bad3.setUserId(1L);
        bad3.setType("DELIVERED");
        bad3.setTitle("  ");
        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad3)))
                .andExpect(status().isBadRequest());
    }
}

