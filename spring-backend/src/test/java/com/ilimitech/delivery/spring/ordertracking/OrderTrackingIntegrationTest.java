package com.ilimitech.delivery.spring.ordertracking;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.ordertracking.dto.CreateOrderTrackingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver","spring.datasource.username=sa","spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop","spring.liquibase.enabled=false","server.servlet.context-path=/api"
})
@AutoConfigureMockMvc @ActiveProfiles("test")
public class OrderTrackingIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void flow_shouldCreateAndList() throws Exception {
        CreateOrderTrackingDto dto = new CreateOrderTrackingDto();
        dto.setOrderId(1L); dto.setCourierId(2L);
        dto.setLatitude(new BigDecimal("40.416775")); dto.setLongitude(new BigDecimal("-3.703790"));
        dto.setStatus("EN_ROUTE");
        mockMvc.perform(post("/order-tracking").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.orderId", is(1))).andExpect(jsonPath("$.status", is("EN_ROUTE")));
        mockMvc.perform(get("/order-tracking")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
        // Negativo: orderId null
        CreateOrderTrackingDto bad = new CreateOrderTrackingDto();
        mockMvc.perform(post("/order-tracking").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }
}

