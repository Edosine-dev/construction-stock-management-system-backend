package com.example.ConstructionStockManagementSystem.repository;

import com.example.ConstructionStockManagementSystem.model.ConstructionSite;
import com.example.ConstructionStockManagementSystem.model.IssueStatus;
import com.example.ConstructionStockManagementSystem.model.StockIssue;
import com.example.ConstructionStockManagementSystem.model.Storekeeper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface StockIssueRepository extends JpaRepository<StockIssue, UUID> {
    List<StockIssue> findByStatusOrderByIssueDateAsc(IssueStatus status);

    List<StockIssue> findByIssueDateBetweenOrderByIssueDateAsc(LocalDate start, LocalDate end);

    boolean existsByStorekeeperAndIssueDateAndStatusNot(
            Storekeeper storekeeper, LocalDate issueDate, IssueStatus status);

    @Query("SELECT DISTINCT s.constructionSite FROM StockIssue s WHERE s.storekeeper.id = :storekeeperId")
    List<ConstructionSite> findConstructionSitesByStorekeeperId(@Param("storekeeperId") UUID storekeeperId);

    @Query("SELECT s.status, COUNT(s) FROM StockIssue s GROUP BY s.status")
    List<Object[]> countIssuesByStatus();

    @Modifying
    @Query("UPDATE StockIssue s SET s.status = com.example.ConstructionStockManagementSystem.model.IssueStatus.REJECTED " +
            "WHERE s.storekeeper.id = :storekeeperId AND s.issueDate = :date " +
            "AND s.status <> com.example.ConstructionStockManagementSystem.model.IssueStatus.ISSUED")
    int cancelIssuesForStorekeeperAndDate(@Param("storekeeperId") UUID storekeeperId,
                                          @Param("date") LocalDate date);

    long countByIssueDate(LocalDate issueDate);

    long countByStatus(IssueStatus status);

    Page<StockIssue> findAll(Pageable pageable);
}
