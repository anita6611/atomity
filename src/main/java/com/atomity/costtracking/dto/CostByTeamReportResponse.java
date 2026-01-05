package com.atomity.costtracking.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CostByTeamReportResponse {

    private UUID tenantId;
    private LocalDate from;
    private LocalDate to;
    private List<TeamCostBreakdown> items;

    public CostByTeamReportResponse() {
    }

    public CostByTeamReportResponse(UUID tenantId, LocalDate from, LocalDate to, List<TeamCostBreakdown> items) {
        this.tenantId = tenantId;
        this.from = from;
        this.to = to;
        this.items = items;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public List<TeamCostBreakdown> getItems() {
        return items;
    }

    public void setItems(List<TeamCostBreakdown> items) {
        this.items = items;
    }
}
