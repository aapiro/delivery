package com.ilimitech.delivery.spring.userwallets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.userwallets.dto.CreateUserWalletDto;
import com.ilimitech.delivery.spring.userwallets.dto.UpdateUserWalletDto;
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
public class UserWalletIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudFlow_shouldWork() throws Exception {
        CreateUserWalletDto create = new CreateUserWalletDto();
        create.setUserId(null);
        create.setBalance(new BigDecimal("10.00"));
        create.setCurrency("EUR");

        String created = mockMvc.perform(post("/user-wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/user-wallets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].currency", is("EUR")));

        var node = objectMapper.readTree(created);
        Long id = node.get("id").asLong();

        mockMvc.perform(get("/user-wallets/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())));

        UpdateUserWalletDto update = new UpdateUserWalletDto();
        update.setBalance(new BigDecimal("20.00"));

        mockMvc.perform(put("/user-wallets/" + id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(20.0)));

        mockMvc.perform(delete("/user-wallets/" + id)).andExpect(status().isNoContent());

        mockMvc.perform(get("/user-wallets").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }
}

