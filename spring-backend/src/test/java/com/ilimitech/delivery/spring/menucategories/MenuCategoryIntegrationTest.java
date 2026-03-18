package com.ilimitech.delivery.spring.menucategories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilimitech.delivery.spring.menucategories.dto.CreateMenuCategoryDto;
import com.ilimitech.delivery.spring.menucategories.dto.UpdateMenuCategoryDto;
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
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "server.servlet.context-path=/api"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MenuCategoryIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void crudFlow_shouldWork_andValidationShouldRejectBadRequests() throws Exception {

        // POST válido
        CreateMenuCategoryDto create = new CreateMenuCategoryDto();
        create.setRestaurantId(1L);
        create.setName("Entrantes");
        create.setSlug("entrantes");
        create.setIsActive(true);

        String createdJson = mockMvc.perform(post("/menu-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name", is("Entrantes")))
                .andExpect(jsonPath("$.slug", is("entrantes")))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createdJson).get("id").asLong();

        // GET list
        mockMvc.perform(get("/menu-categories").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // GET by id
        mockMvc.perform(get("/menu-categories/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())));

        // PUT
        UpdateMenuCategoryDto update = new UpdateMenuCategoryDto();
        update.setName("Entrantes Updated");
        mockMvc.perform(put("/menu-categories/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Entrantes Updated")));

        // DELETE
        mockMvc.perform(delete("/menu-categories/" + id))
                .andExpect(status().isNoContent());

        // GET list vacía
        mockMvc.perform(get("/menu-categories").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // GET by id inexistente -> 404
        mockMvc.perform(get("/menu-categories/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Negativo: restaurantId null -> 400
        CreateMenuCategoryDto bad1 = new CreateMenuCategoryDto();
        bad1.setName("Test");
        bad1.setSlug("test");
        mockMvc.perform(post("/menu-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad1)))
                .andExpect(status().isBadRequest());

        // Negativo: name blank -> 400
        CreateMenuCategoryDto bad2 = new CreateMenuCategoryDto();
        bad2.setRestaurantId(1L);
        bad2.setName("  ");
        bad2.setSlug("test");
        mockMvc.perform(post("/menu-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad2)))
                .andExpect(status().isBadRequest());

        // Negativo: slug blank -> 400
        CreateMenuCategoryDto bad3 = new CreateMenuCategoryDto();
        bad3.setRestaurantId(1L);
        bad3.setName("Test");
        bad3.setSlug("  ");
        mockMvc.perform(post("/menu-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad3)))
                .andExpect(status().isBadRequest());
    }
}

