package com.ilimitech.delivery.spring.referralcodes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.referralcodes.dto.CreateReferralCodeDto;
import com.ilimitech.delivery.spring.referralcodes.dto.UpdateReferralCodeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:referraltestdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "server.servlet.context-path=/api"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReferralCodeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudFlow_shouldWork() throws Exception {
        // CREATE
        CreateReferralCodeDto create = new CreateReferralCodeDto();
        create.setUserId(1L);
        create.setCode("PROMO2026");
        create.setDiscountAmount(new BigDecimal("10.00"));
        create.setMaxUses(100);

        String created = mockMvc.perform(post("/referral-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.code", is("PROMO2026")))
                .andExpect(jsonPath("$.timesUsed", is(0)))
                .andReturn().getResponse().getContentAsString();

        // LIST
        mockMvc.perform(get("/referral-codes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].code", is("PROMO2026")));

        Long id = objectMapper.readTree(created).get("id").asLong();

        // GET BY ID
        mockMvc.perform(get("/referral-codes/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.code", is("PROMO2026")));

        // GET BY ID - NOT FOUND
        mockMvc.perform(get("/referral-codes/9999").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // UPDATE
        UpdateReferralCodeDto update = new UpdateReferralCodeDto();
        update.setCode("VERANO2026");
        update.setDiscountAmount(new BigDecimal("15.00"));

        mockMvc.perform(put("/referral-codes/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("VERANO2026")))
                .andExpect(jsonPath("$.discountAmount", is(15.0)));

        // DELETE
        mockMvc.perform(delete("/referral-codes/" + id))
                .andExpect(status().isNoContent());

        // LIST AFTER DELETE
        mockMvc.perform(get("/referral-codes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void create_withMissingCode_shouldReturnBadRequest() throws Exception {
        CreateReferralCodeDto dto = new CreateReferralCodeDto();
        dto.setUserId(1L);
        // code is null

        mockMvc.perform(post("/referral-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_withMissingUserId_shouldReturnBadRequest() throws Exception {
        CreateReferralCodeDto dto = new CreateReferralCodeDto();
        dto.setCode("TEST");
        // userId is null

        mockMvc.perform(post("/referral-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}

