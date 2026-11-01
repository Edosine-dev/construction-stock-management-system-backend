package com.example.ConstructionStockManagementSystem.repository;

import com.example.ConstructionStockManagementSystem.model.IssuedMaterial;
import com.example.ConstructionStockManagementSystem.model.StockIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IssuedMaterialRepository extends JpaRepository<IssuedMaterial, UUID> {
    boolean existsByStockIssue(StockIssue stockIssue);

    @Query("SELECT m FROM IssuedMaterial m WHERE m.stockIssue.constructionSite.siteCode = :siteCode " +
            "ORDER BY m.issuedDate DESC")
    List<IssuedMaterial> findByConstructionSiteCode(@Param("siteCode") String siteCode);
}
