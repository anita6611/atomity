package com.atomity.costtracking.service;

import com.atomity.costtracking.context.TenantContext;
import com.atomity.costtracking.dto.CreateTeamRequest;
import com.atomity.costtracking.dto.TeamResponse;
import com.atomity.costtracking.exception.DuplicateResourceException;
import com.atomity.costtracking.model.Team;
import com.atomity.costtracking.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional
    public TeamResponse createTeam(CreateTeamRequest request) {
        UUID tenantId = TenantContext.getTenantId();

        if (teamRepository.existsByTenantIdAndName(tenantId, request.getName())) {
            throw new DuplicateResourceException("Team with name '" + request.getName() + "' already exists for this tenant");
        }

        Team team = new Team(tenantId, request.getName());
        team = teamRepository.save(team);

        return new TeamResponse(team.getId(), team.getTenantId(), team.getName());
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> listTeams() {
        UUID tenantId = TenantContext.getTenantId();

        return teamRepository.findByTenantId(tenantId).stream()
                .map(team -> new TeamResponse(team.getId(), team.getTenantId(), team.getName()))
                .collect(Collectors.toList());
    }
}
