package br.dev.hygino.dscatalog.resources;

import br.dev.hygino.dscatalog.dto.ProductRequestDTO;
import br.dev.hygino.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;
    private ProductRequestDTO productRequestDTO;

    @BeforeEach
    public void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
        productRequestDTO = Factory.createProductRequest();
    }

    @Test
    @DisplayName("FindAll deve retornar uma página ordenada por nome")
    public void findAllShouldReturnPageWhenSortByName() throws Exception {
        final ResultActions result = mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON));

        assertAll(
                () -> result.andExpect(status().isOk()),
                () -> result.andExpect(jsonPath("$.content").exists()),
                () -> result.andExpect(jsonPath("$.totalElements").value(countTotalProducts)),
                () -> result.andExpect(jsonPath("$.totalElements").value(countTotalProducts)),
                () -> result.andExpect(jsonPath("$.sort.sorted").value(true)),
                () -> result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro")),
                () -> result.andExpect(jsonPath("$.content[1].name").value("PC Gamer")),
                () -> result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"))
        );
    }

    @Test
    @DisplayName("Update deve retornar um produto com resposta OK")
    public void updateShouldReturnProductWhenIdExists() throws Exception {
        final String jsonBody = objectMapper.writeValueAsString(productRequestDTO);
        final String expectedName = "Phone";
        final double expectedPrice = 800.0;
        final String expectedDescription = "Good Phone";
        final String expectedImgUrl = "https://img.com/img.png";
        //final Instant expectedDate = Instant.parse("2024-12-18T07:12:00Z");

        final ResultActions result = mockMvc.perform(
                put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.price").value(expectedPrice));
        result.andExpect(jsonPath("$.description").value(expectedDescription));
        result.andExpect(jsonPath("$.imgUrl").value(expectedImgUrl));
        //result.andExpect(jsonPath("$.date").value(expectedDate));
    }

    @Test
    @DisplayName("Update deve retornar NOT_FOUND")
    public void updateShouldReturnWhenIdExists() throws Exception {
        final String jsonBody = objectMapper.writeValueAsString(productRequestDTO);

        final ResultActions result = mockMvc.perform(
                put("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

        result.andExpect(jsonPath("$.timestamp").exists());
        result.andExpect(jsonPath("$.status").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.path").exists());
    }

    @Test
    @DisplayName("FindById deve retornar NOT_FOUND quando o Id não existir")
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        final ResultActions result = mockMvc.perform(
                get("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

        result.andExpect(jsonPath("$.timestamp").exists());
        result.andExpect(jsonPath("$.status").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.path").exists());
    }

    @Test
    @DisplayName("FindById deve retornar um produto com resposta OK")
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        final ResultActions result = mockMvc.perform(
                get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));
        final var date = Instant.parse("2020-07-13T20:50:07.12345Z");

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value("The Lord of the Rings"));
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.price").value(90.5));
    }
}
