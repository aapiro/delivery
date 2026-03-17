package com.ilimitech.delivery.spring.addresses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.addresses.dto.CreateAddressDto;
import com.ilimitech.delivery.spring.addresses.dto.UpdateAddressDto;
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
public class AddressIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudFlow_shouldWork() throws Exception {
        // Create
        CreateAddressDto create = CreateAddressDto.builder()
                .street("Calle Falsa 123")
                .city("Springfield")
                .country("ES")
                .zipCode("28001")
                .latitude(new BigDecimal("40.4168"))
                .longitude(new BigDecimal("-3.7038"))
                .build();

        String createdJson = mockMvc.perform(post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getContentAsString();

        // List and assert one element
        mockMvc.perform(get("/addresses").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].street", is("Calle Falsa 123")));

        // Extract id
        var node = objectMapper.readTree(createdJson);
        Long id = node.get("id").asLong();

        // Get by id
        mockMvc.perform(get("/addresses/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())));

        // Update
        UpdateAddressDto update = UpdateAddressDto.builder()
                .street("Calle Nueva 456")
                .build();

        mockMvc.perform(put("/addresses/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street", is("Calle Nueva 456")));

        // Delete
        mockMvc.perform(delete("/addresses/" + id))
                .andExpect(status().isNoContent());

        // Ensure empty
        mockMvc.perform(get("/addresses").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}

