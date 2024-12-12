package br.dev.hygino.dscatalog.resources;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.dev.hygino.dscatalog.dto.ProductDTO;
import br.dev.hygino.dscatalog.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductResource {

    private final ProductService service;

    public ProductResource(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }
}
