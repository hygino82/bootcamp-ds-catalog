package br.dev.hygino.dscatalog.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.dev.hygino.dscatalog.repositories.CategoryRepository;
import br.dev.hygino.dscatalog.repositories.ProductRepository;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	private long existingId;
	private long nonExistingId;
	private long dependentId;

	@BeforeEach
	public void setUp() throws Exception {
		existingId = 1L;
		dependentId = 25L;
		nonExistingId = 1000L;

		Mockito.doNothing().when(productRepository).deleteById(existingId);
		// Mockito.doThrow().when(productRepository).deleteById(nonExistingId);

		Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
		Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);
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
}
