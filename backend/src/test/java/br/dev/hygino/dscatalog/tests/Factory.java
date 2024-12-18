package br.dev.hygino.dscatalog.tests;

import java.time.Instant;

import br.dev.hygino.dscatalog.dto.ProductDTO;
import br.dev.hygino.dscatalog.entities.Category;
import br.dev.hygino.dscatalog.entities.Product;

public class Factory {
	public static Product createProduct() {
		Product product = new Product(
				1L, "Phone", 
				"Good Phone", 
				"https://img.com/img.png", 800.0,
				Instant.parse("2024-12-18T07:12:00Z"));
		product.getCategories().add(new Category(2L, "Eletrônicos"));

		return product;
	}

	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, true);
	}
}
