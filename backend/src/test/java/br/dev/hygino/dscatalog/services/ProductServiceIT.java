package br.dev.hygino.dscatalog.services;

import br.dev.hygino.dscatalog.dto.ProductDTO;
import br.dev.hygino.dscatalog.repositories.ProductRepository;
import br.dev.hygino.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional //garante que a cada teste seja feito roll back do banco de dados
public class ProductServiceIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private long countTotalProducts;

    @BeforeEach
    public void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 3L;
        countTotalProducts = 25L;
    }

    @Test
    @DisplayName("FindById deve retornar o produto quando o id existe")
    public void findByIdShouldReturnProductWhenIdExists() {
        final var result = service.findById(existingId);

        assertNotNull(result);
        assertEquals("The Lord of the Rings", result.name());
        assertEquals(90.5, result.price());
        assertEquals("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg", result.imgUrl());
        assertEquals(Instant.parse("2020-07-13T20:50:07.12345Z"), result.date());
    }

    @Test
    @DisplayName("Delete deve deletar um produto quando o id existe")
    public void deleteShouldDeleteResourceWhenIdExists() {
        assertDoesNotThrow(() -> service.delete(existingId));
        assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    @DisplayName("Delete deve lançar ResourceNotFoundException quando o id não existir")
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
    }

    @Test
    @DisplayName("FindAll deve retornar uma página 0 com 10 elementos")
    public void findAllShouldReturnPageWhenPage0Size10() {
        final PageRequest pageable = PageRequest.of(0, 10);
        final Page<ProductDTO> result = service.findAll(pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    @DisplayName("FindAll deve retornar uma página vazia")
    public void findAllShouldReturnEmptyPageWhenPageDoesNotExists() {
        final PageRequest pageable = PageRequest.of(50, 10);
        final Page<ProductDTO> result = service.findAll(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("FindAll deve retornar uma página ordenada por nome")
    public void findAllShouldReturnSortedPageWhenSortByName() {
        final PageRequest pageable = PageRequest.of(0, 10, Sort.by("name"));
        final Page<ProductDTO> result = service.findAll(pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).name());
        assertEquals("PC Gamer", result.getContent().get(1).name());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).name());
    }
}
