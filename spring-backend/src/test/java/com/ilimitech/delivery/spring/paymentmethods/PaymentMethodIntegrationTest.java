package com.ilimitech.delivery.spring.paymentmethods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.paymentmethods.dto.CreatePaymentMethodDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:paymenttestdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "server.servlet.context-path=/api"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PaymentMethodIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudFlow_shouldWork() throws Exception {
        // CREATE
        CreatePaymentMethodDto create = new CreatePaymentMethodDto();
        create.setUserId(1L);
        create.setType("CREDIT_CARD");
        create.setProvider("VISA");
        create.setLastFour("4242");
        create.setIsDefault(true);
        create.setIsActive(true);

        String created = mockMvc.perform(post("/payment-methods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.type", is("CREDIT_CARD")))
                .andExpect(jsonPath("$.provider", is("VISA")))
                .andExpect(jsonPath("$.lastFour", is("4242")))
                .andExpect(jsonPath("$.isDefault", is(true)))
                .andExpect(jsonPath("$.isActive", is(true)))
                .andReturn().getResponse().getContentAsString();

        // LIST
        mockMvc.perform(get("/payment-methods").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is("CREDIT_CARD")));

        Long id = objectMapper.readTree(created).get("id").asLong();

        // DELETE
        mockMvc.perform(delete("/payment-methods/" + id))
                .andExpect(status().isNoContent());

        // LIST AFTER DELETE
        mockMvc.perform(get("/payment-methods").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void create_withMissingUserId_shouldReturnBadRequest() throws Exception {
        CreatePaymentMethodDto dto = new CreatePaymentMethodDto();
        dto.setType("CREDIT_CARD");
        // userId is null

        mockMvc.perform(post("/payment-methods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_withMissingType_shouldReturnBadRequest() throws Exception {
        CreatePaymentMethodDto dto = new CreatePaymentMethodDto();
        dto.setUserId(1L);
        // type is null

        mockMvc.perform(post("/payment-methods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}

