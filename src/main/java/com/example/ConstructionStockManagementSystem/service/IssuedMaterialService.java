package com.example.ConstructionStockManagementSystem.service;

import com.example.ConstructionStockManagementSystem.model.IssuedMaterial;
import com.example.ConstructionStockManagementSystem.model.IssueStatus;
import com.example.ConstructionStockManagementSystem.model.StockIssue;
import com.example.ConstructionStockManagementSystem.repository.IssuedMaterialRepository;
import com.example.ConstructionStockManagementSystem.repository.StockIssueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IssuedMaterialService {
    private final IssuedMaterialRepository repository;
    private final StockIssueRepository stockIssueRepository;

    public IssuedMaterialService(IssuedMaterialRepository repository,
                                 StockIssueRepository stockIssueRepository) {
        this.repository = repository;
        this.stockIssueRepository = stockIssueRepository;
    }

    public String save(UUID stockIssueId, IssuedMaterial material) {
        Optional<StockIssue> foundIssue = stockIssueRepository.findById(stockIssueId);
        if (foundIssue.isEmpty()) {
            return "The stock issue with that id does not exist";
        }
        StockIssue stockIssue = foundIssue.get();
        if (stockIssue.getStatus() != IssueStatus.ISSUED) {
            return "Issued materials are only for ISSUED stock issues";
        }

        material.setIssuedDate(LocalDate.now());
        material.setStockIssue(stockIssue);
        repository.save(material);
        return "Issued material saved successfully";
    }

    public List<IssuedMaterial> findBySiteCode(String siteCode) {
        return repository.findByConstructionSiteCode(siteCode);
    }

    public String delete(UUID id) {
        if (repository.findById(id).isEmpty()) {
            return "The issued material with that id does not exist";
        }
        repository.deleteById(id);
        return "Issued material deleted successfully";
    }
}
