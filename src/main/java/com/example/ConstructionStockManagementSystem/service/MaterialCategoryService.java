package com.example.ConstructionStockManagementSystem.service;

import com.example.ConstructionStockManagementSystem.model.MaterialCategory;
import com.example.ConstructionStockManagementSystem.repository.MaterialCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MaterialCategoryService {
    private final MaterialCategoryRepository repository;

    public MaterialCategoryService(MaterialCategoryRepository repository) {
        this.repository = repository;
    }

    public List<MaterialCategory> getAll() {
        return repository.findAll();
    }

    public Optional<MaterialCategory> getById(UUID id) {
        return repository.findById(id);
    }

    public String save(MaterialCategory category) {
        repository.save(category);
        return "Material category saved successfully";
    }

    public String update(UUID id, MaterialCategory updatedCategory) {
        return repository.findById(id).map(category -> {
            category.setName(updatedCategory.getName());
            repository.save(category);
            return "Material category updated successfully";
        }).orElse("The material category with that id does not exist");
    }

    public String delete(UUID id) {
        if (repository.findById(id).isEmpty()) {
            return "The material category with that id does not exist";
        }
        repository.deleteById(id);
        return "Material category deleted successfully";
    }

    public List<MaterialCategory> getUnused() {
        return repository.findUnusedCategories();
    }
}
