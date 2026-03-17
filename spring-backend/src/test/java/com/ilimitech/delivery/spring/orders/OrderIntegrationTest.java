package com.ilimitech.delivery.spring.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.orders.dto.CreateOrderDto;
import com.ilimitech.delivery.spring.orders.dto.UpdateOrderDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudFlow_shouldWork() throws Exception {
        CreateOrderDto create = new CreateOrderDto();
        create.setUserId(null);
        create.setRestaurantId(null);
        create.setTotalAmount(new BigDecimal("25.00"));
        create.setDeliveryType("DELIVERY");
        create.setDeliveryAddress("Calle Falsa 1");
        create.setOrderDate(LocalDateTime.now());

        String created = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].totalAmount", is(25.0)));

        var node = objectMapper.readTree(created);
        Long id = node.get("id").asLong();

        mockMvc.perform(get("/orders/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())));

        UpdateOrderDto update = new UpdateOrderDto();
        update.setStatus("PREPARING");

        mockMvc.perform(put("/orders/" + id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("PREPARING")));

        mockMvc.perform(delete("/orders/" + id)).andExpect(status().isNoContent());

        mockMvc.perform(get("/orders").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }
}

