package br.dev.hygino.dscatalog.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.dev.hygino.dscatalog.dto.ProductDTO;
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
}
