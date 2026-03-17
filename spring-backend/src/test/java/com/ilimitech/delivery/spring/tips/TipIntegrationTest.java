package com.ilimitech.delivery.spring.tips;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.tips.dto.CreateTipDto;
import com.ilimitech.delivery.spring.tips.dto.UpdateTipDto;
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
public class TipIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudFlow_shouldWork() throws Exception {
        CreateTipDto create = new CreateTipDto();
        create.setOrderId(null);
        create.setCourierId(null);
        create.setAmount(new BigDecimal("3.50"));
        create.setTipType("POST_ORDER");

        String created = mockMvc.perform(post("/tips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/tips").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].amount", is(3.5)));

        var node = objectMapper.readTree(created);
        Long id = node.get("id").asLong();

        mockMvc.perform(get("/tips/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())));

        UpdateTipDto update = new UpdateTipDto();
        update.setTipType("PRE_ORDER");

        mockMvc.perform(put("/tips/" + id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipType", is("PRE_ORDER")));

        mockMvc.perform(delete("/tips/" + id)).andExpect(status().isNoContent());

        mockMvc.perform(get("/tips").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }
}

