package com.example.ConstructionStockManagementSystem.controller;

import com.example.ConstructionStockManagementSystem.model.IssueStatus;
import com.example.ConstructionStockManagementSystem.model.StockIssue;
import com.example.ConstructionStockManagementSystem.service.StockIssueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stock-issues")
public class StockIssueController {
    private final StockIssueService service;

    public StockIssueController(StockIssueService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody StockIssue stockIssue) {
        String message = service.save(stockIssue);
        if (message.equals("Stock issue saved successfully")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } else if (message.equals("Storekeeper already has a stock issue on that date")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @GetMapping("/all")
    public List<StockIssue> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return service.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The stock issue with that id does not exist"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id,
                                         @RequestBody StockIssue stockIssue) {
        String message = service.update(id, stockIssue);
        if (message.equals("The stock issue with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = service.delete(id);
        if (message.equals("The stock issue with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("That stock issue has issued materials, delete them first")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @GetMapping("/by-status")
    public List<StockIssue> byStatus(@RequestParam IssueStatus status) {
        return service.findByStatus(status);
    }

    @GetMapping("/between")
    public List<StockIssue> between(@RequestParam String start, @RequestParam String end) {
        return service.findBetween(LocalDate.parse(start), LocalDate.parse(end));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @RequestParam IssueStatus status) {
        String message = service.updateStatus(id, status);
        if (message.equals("Stock issue status updated successfully")) {
            return ResponseEntity.ok(message);
        } else if (message.equals("The stock issue with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @GetMapping("/stats/by-status")
    public List<Object[]> statsByStatus() {
        return service.countByStatus();
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID storekeeperId,
                                            @RequestParam String date) {
        if (!service.storekeeperExists(storekeeperId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The storekeeper with that id does not exist");
        }
        int cancelled = service.cancelDay(storekeeperId, LocalDate.parse(date));
        return ResponseEntity.ok(cancelled + " stock issues cancelled");
    }

    @GetMapping("/page")
    public Page<StockIssue> page(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 @RequestParam(defaultValue = "issueDate,desc") String sort) {
        String[] sortParts = sort.split(",", 2);
        Sort.Direction direction = sortParts.length == 2
                ? Sort.Direction.fromString(sortParts[1])
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParts[0]));
        return service.getPage(pageable);
    }
}
