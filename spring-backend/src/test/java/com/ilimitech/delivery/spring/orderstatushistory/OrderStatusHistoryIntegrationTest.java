package com.ilimitech.delivery.spring.orderstatushistory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.orderstatushistory.dto.CreateOrderStatusHistoryDto;
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
        "spring.datasource.driverClassName=org.h2.Driver","spring.datasource.username=sa","spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop","spring.liquibase.enabled=false","server.servlet.context-path=/api"
})
@AutoConfigureMockMvc @ActiveProfiles("test")
public class OrderStatusHistoryIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void flow_shouldCreateAndList() throws Exception {
        CreateOrderStatusHistoryDto dto = new CreateOrderStatusHistoryDto();
        dto.setOrderId(1L); dto.setStatus("CONFIRMED");
        mockMvc.perform(post("/order-status-history").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.status", is("CONFIRMED")));
        mockMvc.perform(get("/order-status-history")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
        // Negativo: orderId null
        CreateOrderStatusHistoryDto bad = new CreateOrderStatusHistoryDto(); bad.setStatus("CREATED");
        mockMvc.perform(post("/order-status-history").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
        // Negativo: status blank
        CreateOrderStatusHistoryDto bad2 = new CreateOrderStatusHistoryDto(); bad2.setOrderId(1L); bad2.setStatus("  ");
        mockMvc.perform(post("/order-status-history").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bad2)))
                .andExpect(status().isBadRequest());
    }
}

