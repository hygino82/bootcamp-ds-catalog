package br.dev.hygino.dscatalog.services;

import br.dev.hygino.dscatalog.repositories.ProductRepository;
import br.dev.hygino.dscatalog.services.exceptions.DatabaseException;
import br.dev.hygino.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
    }

    @Test
    @DisplayName("Delete deve lançar DatabaseException quando o id for dependente")
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        assertThrows(DatabaseException.class, () -> service.delete(dependentId));
        //TODO corrigir método
    }
}
