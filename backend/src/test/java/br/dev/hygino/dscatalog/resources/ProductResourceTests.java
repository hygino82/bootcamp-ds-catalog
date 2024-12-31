package br.dev.hygino.dscatalog.resources;

import br.dev.hygino.dscatalog.dto.ProductDTO;
import br.dev.hygino.dscatalog.dto.ProductRequestDTO;
import br.dev.hygino.dscatalog.services.ProductService;
import br.dev.hygino.dscatalog.services.exceptions.DatabaseException;
import br.dev.hygino.dscatalog.services.exceptions.ResourceNotFoundException;
import br.dev.hygino.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService service;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private ProductRequestDTO productRequestDTO;

    @BeforeEach
    public void setUp() {
        ProductDTO productDTO = Factory.createProductDTO();
        PageImpl<ProductDTO> page = new PageImpl<>(List.of(productDTO));
        existingId = 1L;
        dependentId = 5L;
        nonExistingId = 25L;
        productRequestDTO = Factory.createProductRequest();

        Mockito.when(service.findAll((Pageable) any())).thenReturn(page);

        Mockito.when(service.findById(existingId)).thenReturn(productDTO);
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.update(eq(existingId), any())).thenReturn(productDTO);
        Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(service).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);

        Mockito.when(service.insert(any())).thenReturn(productDTO);
    }

    @Test
    @DisplayName("FindAllPaged deve retornar uma página")
    public void findAllPagedShouldReturnPage() throws Exception {
        final ResultActions result = mockMvc.perform(
                get("/products")
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("FindById deve retornar um produto com resposta OK")
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        final ResultActions result = mockMvc.perform(
                get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.date").exists());
    }

    @Test
    @DisplayName("FindById deve retornar NOT_FOUND")
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
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
    @DisplayName("Update deve retornar um produto com resposta OK")
    public void updateShouldReturnProductWhenIdExists() throws Exception {
        final String jsonBody = objectMapper.writeValueAsString(productRequestDTO);

        final ResultActions result = mockMvc.perform(
                put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.date").exists());
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
    @DisplayName("Insert deve retornar um produto")
    public void insertShouldReturnProduct() throws Exception {
        final String jsonBody = objectMapper.writeValueAsString(productRequestDTO);

        final ResultActions result = mockMvc.perform(
                post("/products")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());

        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.date").exists());
    }

    @Test
    @DisplayName("Delete deve retornar NO_CONTENT")
    public void deleteShouldReturnNoContent() throws Exception {
        final ResultActions result = mockMvc.perform(
                delete("/products/{id}", existingId));
        result.andExpect(status().isNoContent());
        Mockito.verify(service, times(1)).delete(existingId);
    }

    @Test
    @DisplayName("Delete deve retornar NO_FOUND")
    public void deleteShouldReturnNoFoundWhenNonExistingId() throws Exception {
        final ResultActions result = mockMvc.perform(
                delete("/products/{id}", nonExistingId));
        result.andExpect(status().isNotFound());
        Mockito.verify(service, times(1)).delete(nonExistingId);
    }

    @Test
    @DisplayName("Delete deve retornar BAD_REQUEST")
    public void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        final ResultActions result = mockMvc.perform(
                delete("/products/{id}", dependentId));
        result.andExpect(status().isBadRequest());
        Mockito.verify(service, times(1)).delete(dependentId);
    }
}
