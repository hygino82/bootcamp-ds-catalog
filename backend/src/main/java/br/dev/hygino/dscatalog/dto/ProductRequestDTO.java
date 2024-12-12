package br.dev.hygino.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public record ProductRequestDTO(
        String name,
        String description,
        Double price,
        String imgUrl,
        Instant date,
        Set<Long> categories) implements Serializable {
}
