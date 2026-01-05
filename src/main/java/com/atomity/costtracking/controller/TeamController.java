package com.atomity.costtracking.controller;

import com.atomity.costtracking.dto.CreateTeamRequest;
import com.atomity.costtracking.dto.TeamResponse;
import com.atomity.costtracking.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        TeamResponse response = teamService.createTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> listTeams() {
        List<TeamResponse> teams = teamService.listTeams();
        return ResponseEntity.ok(teams);
    }
}
