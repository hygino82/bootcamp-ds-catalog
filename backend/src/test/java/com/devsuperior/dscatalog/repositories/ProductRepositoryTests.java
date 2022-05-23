package com.devsuperior.dscatalog.repositories;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		long existingId = 1L;

		repository.deleteById(existingId);

		Optional<Product> result = repository.findById(existingId);

		Assertions.assertFalse(result.isPresent());// verifica se o valor está presente
	}

	@Test
	public void deleteShoudThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {

		long nonExistingId = 1000L;

		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}

	@Test
	public void createShouldReturnValidObject() {
		Product product = new Product(20L, "Torresmo", "Pele de porco frita na banha.", 6.79, "Torresmo.bmp",
				Instant.now());
		long id = product.getId();

		repository.save(product);

		Optional<Product> result = repository.findById(id);

		Assertions.assertTrue(result.isPresent());
	}
}
