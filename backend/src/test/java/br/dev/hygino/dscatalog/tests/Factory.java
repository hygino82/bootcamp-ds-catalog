package br.dev.hygino.dscatalog.tests;

import br.dev.hygino.dscatalog.dto.ProductDTO;
import br.dev.hygino.dscatalog.dto.ProductRequestDTO;
import br.dev.hygino.dscatalog.entities.Category;
import br.dev.hygino.dscatalog.entities.Product;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Factory {
    public static Category createCategory() {
        return new Category(2L, "Eletrônicos");
    }

    public static Product createProduct() {
        Product product = new Product(
                1L,
                "Phone",
                "Good Phone",
                "https://img.com/img.png",
                800.0,
                Instant.parse("2024-12-18T07:12:00Z"));
        product.getCategories().add(new Category(2L, "Eletrônicos"));

        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, true);
    }

    public static List<Product> createProductList() {
        final Product product1 = new Product(
                1L,
                "Phone",
                "Good Phone",
                "https://img.com/img.png", 800.0,
                Instant.parse("2024-12-18T07:12:00Z"));
        product1.getCategories().add(createCategory());

        final Product product2 = new Product(
                2L,
                "TV",
                "Smart TV",
                "https://img.com/tv.png", 900.45,
                Instant.parse("2024-12-28T07:12:00Z"));
        product2.getCategories().add(createCategory());

        final Product product3 = new Product(
                3L,
                "Radio",
                "Smart TV",
                "https://img.com/radio.png", 45.0,
                Instant.parse("2024-12-30T07:12:00Z"));
        product3.getCategories().add(createCategory());

        return Arrays.asList(product1, product2, product3);
    }

    public static ProductRequestDTO createProductRequest() {
        return new ProductRequestDTO(
                "Phone",
                "Good Phone", 800.0,
                "https://img.com/img.png",
                Instant.parse("2024-12-18T07:12:00Z")
                , Set.of(2L));

    }
}
