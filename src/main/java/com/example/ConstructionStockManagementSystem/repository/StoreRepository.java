package com.example.ConstructionStockManagementSystem.repository;

import com.example.ConstructionStockManagementSystem.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
    Optional<Store> findByStoreNumber(int storeNumber);

    @Query("SELECT s FROM Store s WHERE NOT EXISTS (SELECT k FROM Storekeeper k WHERE k.store = s) ORDER BY s.storeNumber")
    List<Store> findFreeStores();
}
