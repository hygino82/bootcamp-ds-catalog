package br.dev.hygino.dscatalog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.dev.hygino.dscatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT obj FROM Category obj WHERE obj.id = :id")
    Optional<Category> findCategoryById(Long id);
}
