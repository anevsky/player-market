package com.alexnevsky.playermarket.service;

import com.alexnevsky.playermarket.exception.PlayerNotFoundException;
import com.alexnevsky.playermarket.exception.TeamNotFoundException;
import com.alexnevsky.playermarket.model.Player;
import com.alexnevsky.playermarket.model.Team;
import com.alexnevsky.playermarket.repository.PlayerRepository;
import com.alexnevsky.playermarket.repository.TeamRepository;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex Nevsky
 *
 * Date: 23/11/2020
 */
@Slf4j
@AllArgsConstructor
@Service
public class TeamService {

  private final TeamRepository teamRepository;
  private final PlayerRepository playerRepository;

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Team addPlayer(Long id, Long playerId) {
    final Team team = teamRepository.findById(id)
        .orElseThrow(() -> new TeamNotFoundException(id));
    final Player player = playerRepository.findById(playerId)
        .orElseThrow(() -> new PlayerNotFoundException(playerId));
    team.getPlayers().add(player);
    return teamRepository.save(team);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Double addMoney(Long id, Double amount) {
    final Team team = teamRepository.findById(id)
        .orElseThrow(() -> new TeamNotFoundException(id));
    team.setBalance(team.getBalance().add(BigDecimal.valueOf(amount)));
    return teamRepository.save(team).getBalance().doubleValue();
  }
}
