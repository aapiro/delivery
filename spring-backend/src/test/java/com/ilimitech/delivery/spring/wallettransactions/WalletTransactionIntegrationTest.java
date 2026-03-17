package com.ilimitech.delivery.spring.wallettransactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.wallettransactions.dto.CreateWalletTransactionDto;
import com.ilimitech.delivery.spring.wallettransactions.dto.UpdateWalletTransactionDto;
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
public class WalletTransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudFlow_shouldWork() throws Exception {
        CreateWalletTransactionDto create = new CreateWalletTransactionDto();
        create.setWalletId(null);
        create.setType("CREDIT");
        create.setAmount(new BigDecimal("5.00"));
        create.setOrderId(null);
        create.setDescription("Test credit");

        String created = mockMvc.perform(post("/wallet-transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/wallet-transactions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is("CREDIT")));

        var node = objectMapper.readTree(created);
        Long id = node.get("id").asLong();

        mockMvc.perform(get("/wallet-transactions/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())));

        UpdateWalletTransactionDto update = new UpdateWalletTransactionDto();
        update.setAmount(new BigDecimal("7.50"));

        mockMvc.perform(put("/wallet-transactions/" + id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", is(7.5)));

        mockMvc.perform(delete("/wallet-transactions/" + id)).andExpect(status().isNoContent());

        mockMvc.perform(get("/wallet-transactions").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }
}

