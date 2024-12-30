package br.dev.hygino.dscatalog.services;

import br.dev.hygino.dscatalog.dto.CategoryDTO;
import br.dev.hygino.dscatalog.dto.ProductRequestDTO;
import br.dev.hygino.dscatalog.entities.Category;
import br.dev.hygino.dscatalog.entities.Product;
import br.dev.hygino.dscatalog.repositories.CategoryRepository;
import br.dev.hygino.dscatalog.repositories.ProductRepository;
import br.dev.hygino.dscatalog.services.exceptions.DatabaseException;
import br.dev.hygino.dscatalog.services.exceptions.ResourceNotFoundException;
import br.dev.hygino.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.time.Instant;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Pageable pageable;
    private Product product;
    private ProductRequestDTO requestDTO;

    @BeforeEach
    public void setUp() throws Exception {
        existingId = 1L;
        dependentId = 25L;
        nonExistingId = 1000L;
        pageable = PageRequest.of(0, 10);
        final PageImpl<Product> page = new PageImpl<>(Factory.createProductList());
        product = Factory.createProduct();
        requestDTO = Factory.createProductRequest();

        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);

        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(categoryRepository.findAllById(Set.of(2L))).thenReturn(List.of(Factory.createCategory()));

        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
    }

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> service.delete(existingId));

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    @DisplayName("Delete deve lançar ResourceNotFoundException quando o id não existir")
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
    }

    @Test
    @DisplayName("Delete deve lançar DatabaseException quando o id não existir")
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        assertThrows(DatabaseException.class, () -> service.delete(dependentId));
    }

    @Test
    @DisplayName("FindAllPaged deve retornar uma página com três elementos")
    public void findAllPagedShouldReturnPageWith3Elements() {
        final var result = service.findAll(pageable);
        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals("Phone", result.getContent().get(0).name());
        assertEquals("TV", result.getContent().get(1).name());
        assertEquals("Radio", result.getContent().get(2).name());

        Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("FindById deve retornar um ProductDTO quando o id existir")
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        final var result = service.findById(existingId);
        final var category = new CategoryDTO(2L, "Eletrônicos");

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Phone", result.name());
        assertEquals("Good Phone", result.description());
        assertEquals(800.0, result.price());
        assertEquals(Instant.parse("2024-12-18T07:12:00Z"), result.date());
        assertEquals("https://img.com/img.png", result.imgUrl());

        assertTrue(result.categories().stream().anyMatch(c -> category.getId().equals(c.getId())));
        assertTrue(result.categories().stream().anyMatch(c -> category.getName().equals(c.getName())));
    }

    @Test
    @DisplayName("FindById deve lançar ResourceNotFoundException quando o id não existir")
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
    }
}
