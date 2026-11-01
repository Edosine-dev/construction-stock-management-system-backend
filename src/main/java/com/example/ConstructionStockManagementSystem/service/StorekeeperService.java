package com.example.ConstructionStockManagementSystem.service;

import com.example.ConstructionStockManagementSystem.model.MaterialCategory;
import com.example.ConstructionStockManagementSystem.model.Store;
import com.example.ConstructionStockManagementSystem.model.Storekeeper;
import com.example.ConstructionStockManagementSystem.repository.MaterialCategoryRepository;
import com.example.ConstructionStockManagementSystem.repository.StoreRepository;
import com.example.ConstructionStockManagementSystem.repository.StorekeeperRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StorekeeperService {
    private final StorekeeperRepository repository;
    private final MaterialCategoryRepository materialCategoryRepository;
    private final StoreRepository storeRepository;

    public StorekeeperService(StorekeeperRepository repository,
                              MaterialCategoryRepository materialCategoryRepository,
                              StoreRepository storeRepository) {
        this.repository = repository;
        this.materialCategoryRepository = materialCategoryRepository;
        this.storeRepository = storeRepository;
    }

    public List<Storekeeper> getAll() {
        return repository.findAll();
    }

    public Optional<Storekeeper> getById(UUID id) {
        return repository.findById(id);
    }

    public String save(Storekeeper storekeeper) {
        repository.save(storekeeper);
        return "Storekeeper saved successfully";
    }

    public String update(UUID id, Storekeeper updatedStorekeeper) {
        return repository.findById(id).map(storekeeper -> {
            storekeeper.setFirstName(updatedStorekeeper.getFirstName());
            storekeeper.setLastName(updatedStorekeeper.getLastName());
            storekeeper.setPhone(updatedStorekeeper.getPhone());
            storekeeper.setStore(updatedStorekeeper.getStore());
            storekeeper.setMaterialCategories(updatedStorekeeper.getMaterialCategories());
            repository.save(storekeeper);
            return "Storekeeper updated successfully";
        }).orElse("The storekeeper with that id does not exist");
    }

    public String delete(UUID id) {
        if (repository.findById(id).isEmpty()) {
            return "The storekeeper with that id does not exist";
        }
        repository.deleteById(id);
        return "Storekeeper deleted successfully";
    }

    public List<Storekeeper> getByMaterialCategory(String name) {
        return repository.findByMaterialCategoryName(name);
    }

    public String addMaterialCategory(UUID storekeeperId, UUID materialCategoryId) {
        Optional<Storekeeper> foundStorekeeper = repository.findById(storekeeperId);
        if (foundStorekeeper.isEmpty()) {
            return "The storekeeper with that id does not exist";
        }
        Optional<MaterialCategory> foundCategory = materialCategoryRepository.findById(materialCategoryId);
        if (foundCategory.isEmpty()) {
            return "That material category does not exist";
        }

        Storekeeper storekeeper = foundStorekeeper.get();
        MaterialCategory category = foundCategory.get();
        boolean alreadyHasCategory = storekeeper.getMaterialCategories().stream()
                .anyMatch(existing -> existing.getId().equals(category.getId()));
        if (alreadyHasCategory) {
            return "The storekeeper already has that material category";
        }

        storekeeper.getMaterialCategories().add(category);
        repository.save(storekeeper);
        return "Material category added to the storekeeper";
    }

    public String changeStore(UUID storekeeperId, int storeNumber) {
        Optional<Storekeeper> foundStorekeeper = repository.findById(storekeeperId);
        if (foundStorekeeper.isEmpty()) {
            return "The storekeeper with that id does not exist";
        }
        Optional<Store> foundStore = storeRepository.findByStoreNumber(storeNumber);
        if (foundStore.isEmpty()) {
            return "The store with that storeNumber does not exist";
        }

        Storekeeper storekeeper = foundStorekeeper.get();
        Store store = foundStore.get();
        if (repository.existsByStore(store)
                && (storekeeper.getStore() == null || !storekeeper.getStore().getId().equals(store.getId()))) {
            return "That store is taken by another storekeeper";
        }

        storekeeper.setStore(store);
        repository.save(storekeeper);
        return "Store changed successfully";
    }
}
