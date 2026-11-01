package com.example.ConstructionStockManagementSystem.repository;

import com.example.ConstructionStockManagementSystem.model.Store;
import com.example.ConstructionStockManagementSystem.model.Storekeeper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface StorekeeperRepository extends JpaRepository<Storekeeper, UUID> {
    @Query("SELECT DISTINCT k FROM Storekeeper k JOIN k.materialCategories c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Storekeeper> findByMaterialCategoryName(String name);

    boolean existsByStore(Store store);
}
