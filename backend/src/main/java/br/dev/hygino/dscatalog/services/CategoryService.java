package br.dev.hygino.dscatalog.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.dev.hygino.dscatalog.dto.CategoryDTO;
import br.dev.hygino.dscatalog.dto.CategoryRequestDTO;
import br.dev.hygino.dscatalog.entities.Category;
import br.dev.hygino.dscatalog.repositories.CategoryRepository;
import br.dev.hygino.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CategoryDTO insert(CategoryRequestDTO dto) {
        Category entity = new Category();
        entity.setName(dto.name());
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(CategoryDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        final var entity = repository.findCategoryById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found!"));

        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryRequestDTO dto) {
        var entity = repository.getReferenceById(id);

        entity.setName(dto.name());

        return new CategoryDTO(repository.save(entity));

    }
}
