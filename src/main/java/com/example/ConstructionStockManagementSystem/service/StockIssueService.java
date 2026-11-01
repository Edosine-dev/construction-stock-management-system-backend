package com.example.ConstructionStockManagementSystem.service;

import com.example.ConstructionStockManagementSystem.model.IssueStatus;
import com.example.ConstructionStockManagementSystem.model.StockIssue;
import com.example.ConstructionStockManagementSystem.repository.IssuedMaterialRepository;
import com.example.ConstructionStockManagementSystem.repository.StockIssueRepository;
import com.example.ConstructionStockManagementSystem.repository.StorekeeperRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockIssueService {
    private final StockIssueRepository repository;
    private final StorekeeperRepository storekeeperRepository;
    private final IssuedMaterialRepository issuedMaterialRepository;

    public StockIssueService(StockIssueRepository repository,
                             StorekeeperRepository storekeeperRepository,
                             IssuedMaterialRepository issuedMaterialRepository) {
        this.repository = repository;
        this.storekeeperRepository = storekeeperRepository;
        this.issuedMaterialRepository = issuedMaterialRepository;
    }

    public List<StockIssue> getAll() {
        return repository.findAll();
    }

    public Optional<StockIssue> getById(UUID id) {
        return repository.findById(id);
    }

    public String save(StockIssue stockIssue) {
        if (stockIssue.getIssueDate().isBefore(LocalDate.now())) {
            return "Stock issue date cannot be in the past";
        }
        if (repository.existsByStorekeeperAndIssueDateAndStatusNot(
                stockIssue.getStorekeeper(), stockIssue.getIssueDate(), IssueStatus.REJECTED)) {
            return "Storekeeper already has a stock issue on that date";
        }
        stockIssue.setStatus(IssueStatus.REQUESTED);
        repository.save(stockIssue);
        return "Stock issue saved successfully";
    }

    public String update(UUID id, StockIssue updatedIssue) {
        Optional<StockIssue> foundIssue = repository.findById(id);
        if (foundIssue.isEmpty()) {
            return "The stock issue with that id does not exist";
        }

        StockIssue issue = foundIssue.get();
        issue.setIssueDate(updatedIssue.getIssueDate());
        issue.setPurpose(updatedIssue.getPurpose());
        issue.setTruckPlate(updatedIssue.getTruckPlate());
        issue.setConstructionSite(updatedIssue.getConstructionSite());
        issue.setStorekeeper(updatedIssue.getStorekeeper());
        repository.save(issue);
        return "Stock issue updated successfully";
    }

    public String delete(UUID id) {
        Optional<StockIssue> foundIssue = repository.findById(id);
        if (foundIssue.isEmpty()) {
            return "The stock issue with that id does not exist";
        }
        if (issuedMaterialRepository.existsByStockIssue(foundIssue.get())) {
            return "That stock issue has issued materials, delete them first";
        }
        repository.delete(foundIssue.get());
        return "Stock issue deleted successfully";
    }

    public List<StockIssue> findByStatus(IssueStatus status) {
        return repository.findByStatusOrderByIssueDateAsc(status);
    }

    public List<StockIssue> findBetween(LocalDate start, LocalDate end) {
        return repository.findByIssueDateBetweenOrderByIssueDateAsc(start, end);
    }

    public String updateStatus(UUID id, IssueStatus newStatus) {
        Optional<StockIssue> foundIssue = repository.findById(id);
        if (foundIssue.isEmpty()) {
            return "The stock issue with that id does not exist";
        }

        StockIssue issue = foundIssue.get();
        IssueStatus currentStatus = issue.getStatus();
        boolean allowed = (currentStatus == IssueStatus.REQUESTED
                && (newStatus == IssueStatus.APPROVED || newStatus == IssueStatus.REJECTED))
                || (currentStatus == IssueStatus.APPROVED
                && (newStatus == IssueStatus.ISSUED || newStatus == IssueStatus.REJECTED));
        if (!allowed) {
            return "Cannot change status from " + currentStatus + " to " + newStatus;
        }

        issue.setStatus(newStatus);
        repository.save(issue);
        return "Stock issue status updated successfully";
    }

    public List<Object[]> countByStatus() {
        return repository.countIssuesByStatus();
    }

    @Transactional
    public int cancelDay(UUID storekeeperId, LocalDate date) {
        return repository.cancelIssuesForStorekeeperAndDate(storekeeperId, date);
    }

    public Page<StockIssue> getPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public long countToday() {
        return repository.countByIssueDate(LocalDate.now());
    }

    public long countRequested() {
        return repository.countByStatus(IssueStatus.REQUESTED);
    }

    public boolean storekeeperExists(UUID storekeeperId) {
        return storekeeperRepository.existsById(storekeeperId);
    }
}
