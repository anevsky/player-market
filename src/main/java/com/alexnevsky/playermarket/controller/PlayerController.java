package com.alexnevsky.playermarket.controller;

import com.alexnevsky.playermarket.dto.PlayerDto;
import com.alexnevsky.playermarket.exception.PlayerNotFoundException;
import com.alexnevsky.playermarket.mapper.PlayerMarketMapper;
import com.alexnevsky.playermarket.model.Player;
import com.alexnevsky.playermarket.repository.PlayerRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alex Nevsky
 *
 * Date: 18/11/2020
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

  private final PlayerRepository playerRepository;
  private final PlayerMarketMapper mapper;

  @GetMapping
  public List<PlayerDto> getAll() {
    return mapper.toPlayerDtoCollection(playerRepository.findAll());
  }

  @PostMapping
  public PlayerDto createPlayer(@RequestBody PlayerDto newPlayer) {
    final Player player = playerRepository.save(mapper.toPlayerEntity(newPlayer));
    return mapper.toPlayerDto(player);
  }

  @GetMapping("/{id}")
  public PlayerDto getPlayer(@PathVariable Long id) {
    return mapper.toPlayerDto(playerRepository.findById(id)
        .orElseThrow(() -> new PlayerNotFoundException(id)));
  }

  @PutMapping("/{id}")
  public PlayerDto updatePlayer(@RequestBody PlayerDto newPlayer, @PathVariable Long id) {
    return mapper.toPlayerDto(playerRepository.findById(id)
        .map(player -> playerRepository.save(mapper.updatePlayerFromDto(player, newPlayer)))
        .orElseGet(() -> {
          newPlayer.setId(id);
          return playerRepository.save(mapper.toPlayerEntity(newPlayer));
        }));
  }

  @DeleteMapping("/{id}")
  public void deletePlayer(@PathVariable Long id) {
    playerRepository.deleteById(id);
  }
}
