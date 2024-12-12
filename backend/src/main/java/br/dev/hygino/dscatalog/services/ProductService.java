package br.dev.hygino.dscatalog.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.dev.hygino.dscatalog.dto.ProductDTO;
import br.dev.hygino.dscatalog.dto.ProductRequestDTO;
import br.dev.hygino.dscatalog.entities.Category;
import br.dev.hygino.dscatalog.entities.Product;
import br.dev.hygino.dscatalog.repositories.CategoryRepository;
import br.dev.hygino.dscatalog.repositories.ProductRepository;
import br.dev.hygino.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        final Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO insert(ProductRequestDTO dto) {
        if (dto.categories() == null || dto.categories().isEmpty()) {
            throw new IllegalArgumentException("As categorias não podem estar vazias");
        }

        final var categories = categoryRepository
                .findAllById(dto.categories()).stream()
                .collect(Collectors.toSet());

        Product entity = new Product();
        setAttributesFromRequest(dto, entity, categories);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    private void setAttributesFromRequest(ProductRequestDTO dto, Product entity, Set<Category> categories) {
        entity.setDescription(dto.description());
        entity.setName(dto.name());
        entity.setPrice(dto.price());
        entity.getCategories().addAll(categories);
        entity.setImgUrl(dto.imgUrl());
        entity.setDate(dto.date());
    }
}
