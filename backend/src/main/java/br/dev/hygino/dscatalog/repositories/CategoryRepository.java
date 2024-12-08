package br.dev.hygino.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.hygino.dscatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
