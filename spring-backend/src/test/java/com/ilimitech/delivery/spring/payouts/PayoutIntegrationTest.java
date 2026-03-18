package com.ilimitech.delivery.spring.payouts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.payouts.dto.CreatePayoutDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:payouttestdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "server.servlet.context-path=/api"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PayoutIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudFlow_shouldWork() throws Exception {
        // CREATE
        CreatePayoutDto create = new CreatePayoutDto();
        create.setRecipientType("COURIER");
        create.setRecipientId(10L);
        create.setAmount(new BigDecimal("150.00"));
        create.setPeriodStart(LocalDate.of(2026, 3, 1));
        create.setPeriodEnd(LocalDate.of(2026, 3, 31));
        create.setStatus("PENDING");

        mockMvc.perform(post("/payouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.recipientType", is("COURIER")))
                .andExpect(jsonPath("$.recipientId", is(10)))
                .andExpect(jsonPath("$.status", is("PENDING")));

        // LIST
        mockMvc.perform(get("/payouts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].recipientType", is("COURIER")));
    }

    @Test
    void create_withMissingRecipientType_shouldReturnBadRequest() throws Exception {
        CreatePayoutDto dto = new CreatePayoutDto();
        dto.setRecipientId(1L);
        dto.setAmount(new BigDecimal("100.00"));
        // recipientType is null

        mockMvc.perform(post("/payouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_withMissingRecipientId_shouldReturnBadRequest() throws Exception {
        CreatePayoutDto dto = new CreatePayoutDto();
        dto.setRecipientType("RESTAURANT");
        dto.setAmount(new BigDecimal("100.00"));
        // recipientId is null

        mockMvc.perform(post("/payouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_withMissingAmount_shouldReturnBadRequest() throws Exception {
        CreatePayoutDto dto = new CreatePayoutDto();
        dto.setRecipientType("RESTAURANT");
        dto.setRecipientId(1L);
        // amount is null

        mockMvc.perform(post("/payouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}

