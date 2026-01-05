package com.atomity.costtracking.service;

import com.atomity.costtracking.context.TenantContext;
import com.atomity.costtracking.dto.CostRecordResponse;
import com.atomity.costtracking.dto.CreateCostRecordRequest;
import com.atomity.costtracking.exception.DuplicateResourceException;
import com.atomity.costtracking.exception.ResourceNotFoundException;
import com.atomity.costtracking.model.CostRecord;
import com.atomity.costtracking.model.Team;
import com.atomity.costtracking.repository.CostRecordRepository;
import com.atomity.costtracking.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CostRecordService {

    private final CostRecordRepository costRecordRepository;
    private final TeamRepository teamRepository;

    public CostRecordService(CostRecordRepository costRecordRepository, TeamRepository teamRepository) {
        this.costRecordRepository = costRecordRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public CostRecordResponse createCostRecord(CreateCostRecordRequest request) {
        UUID tenantId = TenantContext.getTenantId();

        Team team = teamRepository.findByIdAndTenantId(request.getTeamId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        if (costRecordRepository.existsByTeamIdAndDate(request.getTeamId(), request.getDate())) {
            throw new DuplicateResourceException("Cost record already exists for this team and date");
        }

        CostRecord costRecord = new CostRecord(request.getTeamId(), request.getDate(), request.getCostAmount());
        costRecord = costRecordRepository.save(costRecord);

        return new CostRecordResponse(costRecord.getId(), costRecord.getTeamId(),
                costRecord.getDate(), costRecord.getCostAmount());
    }

    @Transactional(readOnly = true)
    public List<CostRecordResponse> listCostRecords(LocalDate from, LocalDate to) {
        UUID tenantId = TenantContext.getTenantId();

        List<UUID> teamIds = teamRepository.findByTenantId(tenantId).stream()
                .map(Team::getId)
                .collect(Collectors.toList());

        if (teamIds.isEmpty()) {
            return List.of();
        }

        return costRecordRepository.findByTeamIdsAndDateRange(teamIds, from, to).stream()
                .map(cr -> new CostRecordResponse(cr.getId(), cr.getTeamId(), cr.getDate(), cr.getCostAmount()))
                .collect(Collectors.toList());
    }
}
