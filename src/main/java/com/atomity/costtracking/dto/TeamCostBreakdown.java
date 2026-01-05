package com.atomity.costtracking.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class TeamCostBreakdown {

    private UUID teamId;
    private String teamName;
    private BigDecimal totalCost;

    public TeamCostBreakdown() {
    }

    public TeamCostBreakdown(UUID teamId, String teamName, BigDecimal totalCost) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.totalCost = totalCost;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
