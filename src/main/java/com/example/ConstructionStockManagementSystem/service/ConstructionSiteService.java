package com.example.ConstructionStockManagementSystem.service;

import com.example.ConstructionStockManagementSystem.model.ConstructionSite;
import com.example.ConstructionStockManagementSystem.repository.ConstructionSiteRepository;
import com.example.ConstructionStockManagementSystem.repository.StockIssueRepository;
import com.example.ConstructionStockManagementSystem.repository.StorekeeperRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConstructionSiteService {
    private final ConstructionSiteRepository repository;
    private final StockIssueRepository stockIssueRepository;
    private final StorekeeperRepository storekeeperRepository;

    public ConstructionSiteService(ConstructionSiteRepository repository,
                                   StockIssueRepository stockIssueRepository,
                                   StorekeeperRepository storekeeperRepository) {
        this.repository = repository;
        this.stockIssueRepository = stockIssueRepository;
        this.storekeeperRepository = storekeeperRepository;
    }

    public List<ConstructionSite> getAll() {
        return repository.findAll();
    }

    public Optional<ConstructionSite> getById(UUID id) {
        return repository.findById(id);
    }

    public String save(ConstructionSite site) {
        repository.save(site);
        return "Construction site saved successfully";
    }

    public String update(UUID id, ConstructionSite updatedSite) {
        return repository.findById(id).map(site -> {
            site.setSiteName(updatedSite.getSiteName());
            site.setForemanName(updatedSite.getForemanName());
            site.setSiteCode(updatedSite.getSiteCode());
            site.setLocation(updatedSite.getLocation());
            repository.save(site);
            return "Construction site updated successfully";
        }).orElse("The construction site with that id does not exist");
    }

    public String delete(UUID id) {
        if (repository.findById(id).isEmpty()) {
            return "The construction site with that id does not exist";
        }
        repository.deleteById(id);
        return "Construction site deleted successfully";
    }

    public List<ConstructionSite> findByLocation(String location) {
        return repository.findByLocationIgnoreCaseOrderBySiteNameAsc(location);
    }

    public List<ConstructionSite> findByStorekeeperId(UUID storekeeperId) {
        return stockIssueRepository.findConstructionSitesByStorekeeperId(storekeeperId);
    }

    public boolean storekeeperExists(UUID storekeeperId) {
        return storekeeperRepository.existsById(storekeeperId);
    }
}
