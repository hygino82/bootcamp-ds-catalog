package br.dev.hygino.dscatalog.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.dev.hygino.dscatalog.entities.Product;
import br.dev.hygino.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;

	@BeforeEach
	public void setUp() throws Exception {
		existingId = 1L;
		countTotalProducts = 25L;
		nonExistingId = 1000L;
	}

	@Autowired
	private ProductRepository repository;

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {

		repository.deleteById(existingId);

		final var result = repository.findById(existingId);
		assertFalse(result.isPresent());
	}

	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);

		product = repository.save(product);

		assertNotNull(product.getId());
		assertEquals(countTotalProducts + 1, product.getId());
	}

	@Test
	public void findByIdShouldReturnNotEmptyWhenIdExists() {
		final var product = repository.findById(existingId);

		assertTrue(!product.isEmpty());
	}

	@Test
	public void findByIdShouldReturnEmptyWhenIdDoesNotExists() {
		final var product = repository.findById(nonExistingId);

		assertTrue(product.isEmpty());
	}
}
