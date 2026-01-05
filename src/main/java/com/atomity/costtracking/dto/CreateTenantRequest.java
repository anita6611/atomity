package com.atomity.costtracking.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateTenantRequest {

    @NotBlank(message = "Name is required")
    private String name;

    public CreateTenantRequest() {
    }

    public CreateTenantRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
