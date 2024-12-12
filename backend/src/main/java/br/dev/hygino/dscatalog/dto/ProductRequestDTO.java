package br.dev.hygino.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequestDTO(
        @NotBlank String name,
        String description,
        @NotNull Double price,
        String imgUrl,
        @NotNull Instant date,
        @Size(min = 1, message = "Produto deve ter pelo menos uma categoria!") Set<Long> categories)
        implements Serializable {
}
