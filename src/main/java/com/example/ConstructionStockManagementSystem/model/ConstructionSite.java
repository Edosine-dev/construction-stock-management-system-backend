package com.example.ConstructionStockManagementSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "construction_site")
public class ConstructionSite {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String siteName;
    private String foremanName;

    @Column(unique = true)
    private String siteCode;

    private String location;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getForemanName() {
        return foremanName;
    }

    public void setForemanName(String foremanName) {
        this.foremanName = foremanName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
