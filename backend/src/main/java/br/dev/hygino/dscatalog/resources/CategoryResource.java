package br.dev.hygino.dscatalog.resources;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.dev.hygino.dscatalog.entities.Category;

@RestController
@RequestMapping("/categories")
public class CategoryResource {

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        final var list = Arrays.asList(
                new Category(1L, "Books"),
                new Category(2L, "Electronics"));
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}
