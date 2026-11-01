package com.example.ConstructionStockManagementSystem.repository;

import com.example.ConstructionStockManagementSystem.model.ConstructionSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConstructionSiteRepository extends JpaRepository<ConstructionSite, UUID> {
    List<ConstructionSite> findByLocationIgnoreCaseOrderBySiteNameAsc(String location);
}
