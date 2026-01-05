package com.atomity.costtracking.dto;

import java.util.UUID;

public class TeamResponse {

    private UUID id;
    private UUID tenantId;
    private String name;

    public TeamResponse() {
    }

    public TeamResponse(UUID id, UUID tenantId, String name) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
