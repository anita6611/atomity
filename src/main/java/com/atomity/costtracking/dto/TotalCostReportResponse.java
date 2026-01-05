package com.atomity.costtracking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TotalCostReportResponse {

    private UUID tenantId;
    private LocalDate from;
    private LocalDate to;
    private BigDecimal totalCost;

    public TotalCostReportResponse() {
    }

    public TotalCostReportResponse(UUID tenantId, LocalDate from, LocalDate to, BigDecimal totalCost) {
        this.tenantId = tenantId;
        this.from = from;
        this.to = to;
        this.totalCost = totalCost;
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

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
