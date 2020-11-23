package com.alexnevsky.playermarket.service;

import com.alexnevsky.playermarket.dto.TransferEvent;
import com.alexnevsky.playermarket.exception.PlayerNotFoundException;
import com.alexnevsky.playermarket.exception.TeamNotFoundException;
import com.alexnevsky.playermarket.model.Player;
import com.alexnevsky.playermarket.model.Team;
import com.alexnevsky.playermarket.repository.PlayerRepository;
import com.alexnevsky.playermarket.repository.TeamRepository;
import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex Nevsky
 *
 * Date: 18/11/2020
 */
@Slf4j
@AllArgsConstructor
@Service
public class PlayerTransferService {

  private final TeamRepository teamRepository;
  private final PlayerRepository playerRepository;

  public BigDecimal getTransferFee(int playerExperienceMonths, int playerAge) {
    return BigDecimal.valueOf(playerExperienceMonths * 100_000 / playerAge);
  }

  public BigDecimal getContractFee(int playerExperienceMonths, int playerAge, double teamCommission) {
    BigDecimal transferFee = getTransferFee(playerExperienceMonths, playerAge);
    return transferFee.add(transferFee.multiply(BigDecimal.valueOf(teamCommission)));
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public TransferEvent transfer(Long from, Long to, Long playerId) {
    final Player player = playerRepository.findById(playerId)
        .orElseThrow(() -> new PlayerNotFoundException(playerId));

    final Team sourceTeam = teamRepository.findById(from)
        .orElseThrow(() -> new TeamNotFoundException(from));

    final Team targetTeam = teamRepository.findById(to)
        .orElseThrow(() -> new TeamNotFoundException(to));

    final BigDecimal contractFee = getContractFee(player.getExperienceMonths(), player.getAge(),
        sourceTeam.getCommission().doubleValue());

    TransferEvent event = TransferEvent.builder()
        .id(UUID.randomUUID())
        .iteration(1L)
        .player(player.toString())
        .sourceTeam(sourceTeam.toString())
        .targetTeam(targetTeam.toString())
        .transferFee(getTransferFee(player.getExperienceMonths(), player.getAge()))
        .contractFee(contractFee)
        .status("INITIATED")
        .time(Instant.now(Clock.systemUTC()))
        .build();

    if (targetTeam.getBalance().compareTo(contractFee) >= 0) {
      targetTeam.setBalance(targetTeam.getBalance().subtract(contractFee));
      sourceTeam.getPlayers().remove(player);
      targetTeam.getPlayers().add(player);
      teamRepository.saveAll(Lists.newArrayList(sourceTeam, targetTeam));
      event.setStatus("SUCCESS");
    } else {
      event.setStatus("FAIL_NO_MONEY");
    }

    log.info("{}", event);

    return event;
  }
}
