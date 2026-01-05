package com.atomity.costtracking.dto;

import java.util.UUID;

public class TenantResponse {

    private UUID id;
    private String name;

    public TenantResponse() {
    }

    public TenantResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
