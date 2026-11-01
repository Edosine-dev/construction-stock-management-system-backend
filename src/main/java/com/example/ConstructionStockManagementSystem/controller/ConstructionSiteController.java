package com.example.ConstructionStockManagementSystem.controller;

import com.example.ConstructionStockManagementSystem.model.ConstructionSite;
import com.example.ConstructionStockManagementSystem.service.ConstructionSiteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/construction-sites")
public class ConstructionSiteController {
    private final ConstructionSiteService service;

    public ConstructionSiteController(ConstructionSiteService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ConstructionSite site) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(site));
    }

    @GetMapping("/all")
    public List<ConstructionSite> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return service.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The construction site with that id does not exist"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id,
                                         @RequestBody ConstructionSite site) {
        String message = service.update(id, site);
        if (message.equals("The construction site with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = service.delete(id);
        if (message.equals("The construction site with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @GetMapping("/by-location")
    public List<ConstructionSite> byLocation(@RequestParam String location) {
        return service.findByLocation(location);
    }

    @GetMapping("/of-storekeeper/{storekeeperId}")
    public ResponseEntity<?> ofStorekeeper(@PathVariable UUID storekeeperId) {
        if (!service.storekeeperExists(storekeeperId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The storekeeper with that id does not exist");
        }
        return ResponseEntity.ok(service.findByStorekeeperId(storekeeperId));
    }
}
