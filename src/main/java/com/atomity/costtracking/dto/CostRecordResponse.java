package com.atomity.costtracking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CostRecordResponse {

    private UUID id;
    private UUID teamId;
    private LocalDate date;
    private BigDecimal costAmount;

    public CostRecordResponse() {
    }

    public CostRecordResponse(UUID id, UUID teamId, LocalDate date, BigDecimal costAmount) {
        this.id = id;
        this.teamId = teamId;
        this.date = date;
        this.costAmount = costAmount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }
}
