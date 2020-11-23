package com.alexnevsky.playermarket.controller;

import static java.util.Objects.isNull;

import com.alexnevsky.playermarket.dto.TeamDto;
import com.alexnevsky.playermarket.exception.TeamNotFoundException;
import com.alexnevsky.playermarket.mapper.PlayerMarketMapper;
import com.alexnevsky.playermarket.model.Team;
import com.alexnevsky.playermarket.repository.PlayerRepository;
import com.alexnevsky.playermarket.repository.TeamRepository;
import com.alexnevsky.playermarket.service.TeamService;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alex Nevsky
 *
 * Date: 18/11/2020
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

  private final TeamService teamService;
  private final TeamRepository teamRepository;
  private final PlayerRepository playerRepository;
  private final PlayerMarketMapper mapper;

  @GetMapping
  public List<TeamDto> getAll(@RequestParam(required = false) Collection<Long> ids) {
    if (isNull(ids)) {
      return mapper.toTeamDtoCollection(teamRepository.findAll());
    } else {
      return mapper.toTeamDtoCollection(teamRepository.findAllByIdIn(ids));
    }
  }

  @PostMapping
  public TeamDto createTeam(@RequestBody TeamDto newTeam) {
    final Team team = teamRepository.save(mapper.toTeamEntity(newTeam));
    return mapper.toTeamDto(team);
  }

  @GetMapping("/{id}")
  public TeamDto getTeam(@PathVariable Long id) {
    return mapper.toTeamDto(teamRepository.findById(id)
        .orElseThrow(() -> new TeamNotFoundException(id)));
  }

  @PutMapping("/{id}")
  public TeamDto updateTeam(@RequestBody TeamDto newTeam, @PathVariable Long id) {
    return mapper.toTeamDto(teamRepository.findById(id)
        .map(team -> teamRepository.save(mapper.updateTeamFromDto(team, newTeam)))
        .orElseGet(() -> {
          newTeam.setId(id);
          return teamRepository.save(mapper.toTeamEntity(newTeam));
        }));
  }

  @DeleteMapping("/{id}")
  public void deleteTeam(@PathVariable Long id) {
    teamRepository.deleteById(id);
  }

  @PatchMapping("/{id}/player")
  public TeamDto addPlayer(@PathVariable Long id, @RequestParam(name = "id") Long playerId) {
    return mapper.toTeamDto(teamService.addPlayer(id, playerId));
  }

  @PatchMapping("/{id}/balance")
  public Double addMoney(@PathVariable Long id, @RequestParam Double amount) {
    return teamService.addMoney(id, amount);
  }
}
