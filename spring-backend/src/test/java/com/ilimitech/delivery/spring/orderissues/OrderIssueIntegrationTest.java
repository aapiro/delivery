package com.ilimitech.delivery.spring.orderissues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.orderissues.dto.CreateOrderIssueDto;
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
public class OrderIssueIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void flow_shouldCreateListAndDelete() throws Exception {
        CreateOrderIssueDto dto = new CreateOrderIssueDto();
        dto.setOrderId(1L); dto.setType("LATE"); dto.setDescription("El pedido llegó tarde");
        String created = mockMvc.perform(post("/order-issues").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.orderId", is(1))).andReturn().getResponse().getContentAsString();
        mockMvc.perform(get("/order-issues")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
        Long id = objectMapper.readTree(created).get("id").asLong();
        mockMvc.perform(delete("/order-issues/" + id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/order-issues")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        // Negativo: orderId null
        CreateOrderIssueDto bad = new CreateOrderIssueDto();
        mockMvc.perform(post("/order-issues").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }
}

