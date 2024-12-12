package br.dev.hygino.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.hygino.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
