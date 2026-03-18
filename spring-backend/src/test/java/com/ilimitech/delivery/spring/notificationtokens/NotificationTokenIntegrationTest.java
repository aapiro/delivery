package com.ilimitech.delivery.spring.notificationtokens;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.notificationtokens.dto.CreateNotificationTokenDto;
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
public class NotificationTokenIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void flow_shouldCreateAndList_andRejectInvalidRequests() throws Exception {

        // POST válido
        CreateNotificationTokenDto dto = new CreateNotificationTokenDto();
        dto.setUserId(1L);
        dto.setToken("abc123devicetoken");
        dto.setPlatform("ANDROID");

        mockMvc.perform(post("/notification-tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.platform", is("ANDROID")));

        // GET lista con 1 elemento
        mockMvc.perform(get("/notification-tokens").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // Negativo: userId null -> 400
        CreateNotificationTokenDto bad1 = new CreateNotificationTokenDto();
        bad1.setToken("sometoken");
        mockMvc.perform(post("/notification-tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad1)))
                .andExpect(status().isBadRequest());

        // Negativo: token blank -> 400
        CreateNotificationTokenDto bad2 = new CreateNotificationTokenDto();
        bad2.setUserId(1L);
        bad2.setToken("  ");
        mockMvc.perform(post("/notification-tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad2)))
                .andExpect(status().isBadRequest());
    }
}

