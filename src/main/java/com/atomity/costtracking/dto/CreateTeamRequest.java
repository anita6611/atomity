package com.atomity.costtracking.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateTeamRequest {

    @NotBlank(message = "Name is required")
    private String name;

    public CreateTeamRequest() {
    }

    public CreateTeamRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
