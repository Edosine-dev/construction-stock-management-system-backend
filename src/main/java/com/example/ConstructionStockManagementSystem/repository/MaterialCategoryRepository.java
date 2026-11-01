package com.example.ConstructionStockManagementSystem.repository;

import com.example.ConstructionStockManagementSystem.model.MaterialCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MaterialCategoryRepository extends JpaRepository<MaterialCategory, UUID> {
    @Query("SELECT c FROM MaterialCategory c WHERE c.storekeepers IS EMPTY")
    List<MaterialCategory> findUnusedCategories();
}
