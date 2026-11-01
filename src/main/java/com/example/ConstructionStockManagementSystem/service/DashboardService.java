package com.example.ConstructionStockManagementSystem.service;

import com.example.ConstructionStockManagementSystem.model.ConstructionSite;
import com.example.ConstructionStockManagementSystem.model.Storekeeper;
import com.example.ConstructionStockManagementSystem.repository.ConstructionSiteRepository;
import com.example.ConstructionStockManagementSystem.repository.IssuedMaterialRepository;
import com.example.ConstructionStockManagementSystem.repository.StoreRepository;
import com.example.ConstructionStockManagementSystem.repository.StorekeeperRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {
    private final ConstructionSiteRepository constructionSiteRepository;
    private final StorekeeperRepository storekeeperRepository;
    private final StoreRepository storeRepository;
    private final StockIssueService stockIssueService;
    private final IssuedMaterialRepository issuedMaterialRepository;

    public DashboardService(ConstructionSiteRepository constructionSiteRepository,
                            StorekeeperRepository storekeeperRepository,
                            StoreRepository storeRepository,
                            StockIssueService stockIssueService,
                            IssuedMaterialRepository issuedMaterialRepository) {
        this.constructionSiteRepository = constructionSiteRepository;
        this.storekeeperRepository = storekeeperRepository;
        this.storeRepository = storeRepository;
        this.stockIssueService = stockIssueService;
        this.issuedMaterialRepository = issuedMaterialRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("sites", constructionSiteRepository.count());
        summary.put("storekeepers", storekeeperRepository.count());
        summary.put("freeStores", (long) storeRepository.findFreeStores().size());
        summary.put("issuesToday", stockIssueService.countToday());
        summary.put("requested", stockIssueService.countRequested());
        summary.put("issuedMaterials", issuedMaterialRepository.count());
        return summary;
    }
}