package br.dev.hygino.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import br.dev.hygino.dscatalog.entities.Product;

public record ProductDTO(
        Long id,
        String name,
        String description,
        Double price,
        String imgUrl,
        Set<CategoryDTO> categories,
        Instant date) implements Serializable {
    public ProductDTO(Product entity, boolean showCategories) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getImgUrl(),
                showCategories ? entity.getCategories().stream().map(CategoryDTO::new).collect(Collectors.toSet())
                        : new HashSet<>(),
                entity.getDate());
    }
}
