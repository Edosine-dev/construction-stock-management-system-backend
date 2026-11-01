package com.example.ConstructionStockManagementSystem.controller;

import com.example.ConstructionStockManagementSystem.model.IssuedMaterial;
import com.example.ConstructionStockManagementSystem.service.IssuedMaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/issued-materials")
public class IssuedMaterialController {
    private final IssuedMaterialService service;

    public IssuedMaterialController(IssuedMaterialService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam UUID stockIssueId,
                                       @RequestBody IssuedMaterial material) {
        String message = service.save(stockIssueId, material);
        if (message.equals("Issued material saved successfully")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } else if (message.equals("Issued materials are only for ISSUED stock issues")) {
            return ResponseEntity.badRequest().body(message);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @GetMapping("/construction-site/{siteCode}")
    public List<IssuedMaterial> byConstructionSite(@PathVariable String siteCode) {
        return service.findBySiteCode(siteCode);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = service.delete(id);
        if (message.equals("The issued material with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }
}
