package br.dev.hygino.dscatalog.resources;

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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

    @Autowired
    private MockMvc mockMvc;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    @BeforeEach
    public void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
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
}
