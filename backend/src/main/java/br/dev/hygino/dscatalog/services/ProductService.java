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
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

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
        return productRepository.findAll(pageable).map(x -> new ProductDTO(x, false));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        final Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));

        return new ProductDTO(entity, true);
    }

    @Transactional
    public ProductDTO insert(@Valid ProductRequestDTO dto) {

        final Set<Category> categories = categoryRepository
                .findAllById(dto.categories()).stream()
                .collect(Collectors.toSet());

        if (categories == null || categories.isEmpty()) {
            throw new ResourceNotFoundException("A lista não tem nenhuma Categoria válida");
        }

        Product entity = new Product();
        setAttributesFromRequest(dto, entity, categories);
        entity = productRepository.save(entity);
        return new ProductDTO(entity, true);
    }

    private void setAttributesFromRequest(ProductRequestDTO dto, Product entity, Set<Category> categories) {
        entity.setDescription(dto.description());
        entity.setName(dto.name());
        entity.setPrice(dto.price());
        entity.getCategories().clear();
        entity.getCategories().addAll(categories);
        entity.setImgUrl(dto.imgUrl());
        entity.setDate(dto.date());
    }

    @Transactional
    public ProductDTO update(Long id, ProductRequestDTO dto) {

        final Set<Category> categories = categoryRepository
                .findAllById(dto.categories()).stream()
                .collect(Collectors.toSet());

        if (categories == null || categories.isEmpty()) {
            throw new ResourceNotFoundException("A lista não tem nenhuma Categoria válida");
        }

        try {
            Product entity = productRepository.getReferenceById(id);
            setAttributesFromRequest(dto, entity, categories);
            entity = productRepository.save(entity);
            return new ProductDTO(entity, true);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }
}
