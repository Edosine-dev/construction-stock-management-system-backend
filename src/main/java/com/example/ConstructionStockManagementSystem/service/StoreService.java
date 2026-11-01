package com.example.ConstructionStockManagementSystem.service;

import com.example.ConstructionStockManagementSystem.model.Store;
import com.example.ConstructionStockManagementSystem.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StoreService {
    private final StoreRepository repository;

    public StoreService(StoreRepository repository) {
        this.repository = repository;
    }

    public List<Store> getAll() {
        return repository.findAll();
    }

    public Optional<Store> getById(UUID id) {
        return repository.findById(id);
    }

    public String save(Store store) {
        repository.save(store);
        return "Store saved successfully";
    }

    public String update(UUID id, Store updatedStore) {
        return repository.findById(id).map(store -> {
            store.setStoreNumber(updatedStore.getStoreNumber());
            store.setName(updatedStore.getName());
            repository.save(store);
            return "Store updated successfully";
        }).orElse("The store with that id does not exist");
    }

    public String delete(UUID id) {
        if (repository.findById(id).isEmpty()) {
            return "The store with that id does not exist";
        }
        repository.deleteById(id);
        return "Store deleted successfully";
    }

    public List<Store> getFreeStores() {
        return repository.findFreeStores();
    }
}
