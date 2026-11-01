package com.example.ConstructionStockManagementSystem.controller;

import com.example.ConstructionStockManagementSystem.model.Storekeeper;
import com.example.ConstructionStockManagementSystem.service.StorekeeperService;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/storekeepers")
public class StorekeeperController {
    private final StorekeeperService service;

    public StorekeeperController(StorekeeperService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Storekeeper storekeeper) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(storekeeper));
    }

    @GetMapping("/all")
    public List<Storekeeper> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return service.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The storekeeper with that id does not exist"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id,
                                         @RequestBody Storekeeper storekeeper) {
        String message = service.update(id, storekeeper);
        if (message.equals("The storekeeper with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = service.delete(id);
        if (message.equals("The storekeeper with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @GetMapping("/by-material-category")
    public List<Storekeeper> byMaterialCategory(@RequestParam String name) {
        return service.getByMaterialCategory(name);
    }

    @PutMapping("/{storekeeperId}/material-categories/{materialCategoryId}")
    public ResponseEntity<String> addMaterialCategory(@PathVariable UUID storekeeperId,
                                                       @PathVariable UUID materialCategoryId) {
        String message = service.addMaterialCategory(storekeeperId, materialCategoryId);
        if (message.equals("Material category added to the storekeeper")) {
            return ResponseEntity.ok(message);
        } else if (message.equals("The storekeeper already has that material category")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @PatchMapping("/{storekeeperId}/store")
    public ResponseEntity<String> changeStore(@PathVariable UUID storekeeperId,
                                               @RequestParam int storeNumber) {
        String message = service.changeStore(storekeeperId, storeNumber);
        if (message.equals("Store changed successfully")) {
            return ResponseEntity.ok(message);
        } else if (message.equals("That store is taken by another storekeeper")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }
}
