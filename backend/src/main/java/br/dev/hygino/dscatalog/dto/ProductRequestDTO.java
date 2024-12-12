package br.dev.hygino.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import jakarta.validation.constraints.Size;

public record ProductRequestDTO(
                String name,
                String description,
                Double price,
                String imgUrl,
                Instant date,
                @Size(min = 1, message = "Produto deve ter pelo menos uma categoria!") Set<Long> categories)
                implements Serializable {
}
