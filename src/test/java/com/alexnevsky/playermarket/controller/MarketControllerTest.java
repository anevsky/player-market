package com.alexnevsky.playermarket.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alexnevsky.playermarket.model.Player;
import com.alexnevsky.playermarket.model.Team;
import com.alexnevsky.playermarket.repository.PlayerRepository;
import com.alexnevsky.playermarket.repository.TeamRepository;
import com.alexnevsky.playermarket.service.PlayerTransferService;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Alex Nevsky
 *
 * Date: 20/11/2020
 */
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MarketControllerTest {

  @Autowired
  private MarketController controller;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TeamRepository teamRepository;

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private PlayerTransferService playerTransferService;

  @Test
  void test_addPlayer_thenOk() throws Exception {
    // given
    final long id = 1;
    final String name = "Real";
    final double balance1 = 100;
    final double balance2 = 1_000_000;
    final double commission = 0.075;
    final Currency currency = Currency.getInstance("USD");

    teamRepository.saveAll(List.of(
        createTestTeam(id, name, balance1, commission, currency),
        createTestTeam(id + 1, name + 1, balance2, commission, currency))
    );

    final long playerId = 1;
    final String playerName = "Ronaldo";
    final int playerAge = 35;
    final int playerExperienceMonths = 200;

    Player player = createTestPlayer(playerId, playerName, playerAge, playerExperienceMonths);
    player = playerRepository.save(player);
    final Team team1 = teamRepository.findById(id).orElseThrow();
    team1.getPlayers().add(player);
    teamRepository.save(team1);

    mockMvc.perform(post("/api/v1/player-market/transfer")
        .contentType("application/json")
        .param("from", String.valueOf(1L))
        .param("to", String.valueOf(2))
        .param("playerId", String.valueOf(playerId)))
        .andExpect(status().isOk());

    // when
    final Team result = teamRepository.findById(2L).orElseThrow();

    final Set<Player> players = result.getPlayers();

    // then
    assertNotNull(result);
    assertThat(result.getId()).isEqualTo(id + 1);
    assertThat(result.getName()).isEqualTo(name + 1);
    assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(balance2).subtract(playerTransferService.getContractFee(playerExperienceMonths, playerAge, commission)));
    assertThat(result.getCommission()).isEqualTo(BigDecimal.valueOf(commission));
    assertThat(result.getCurrency()).isEqualTo(currency);
    assertNotNull(players);
    assertThat(players.size()).isEqualTo(1);
    assertThat(players.stream().filter(p -> p.getId().equals(playerId)).findAny().orElseThrow().getName()).isEqualTo(playerName);
    assertThat(players.stream().filter(p -> p.getId().equals(playerId)).findAny().orElseThrow().getAge()).isEqualTo(playerAge);
    assertThat(players.stream().filter(p -> p.getId().equals(playerId)).findAny().orElseThrow().getExperienceMonths()).isEqualTo(playerExperienceMonths);
  }

  @Test
  void test_addPlayer_thenFail_becauseNoMoney() throws Exception {
    // given
    final long id = 1;
    final String name = "Real";
    final double balance1 = 100;
    final double balance2 = 200;
    final double commission = 0.075;
    final Currency currency = Currency.getInstance("USD");

    teamRepository.saveAll(List.of(
        createTestTeam(id, name, balance1, commission, currency),
        createTestTeam(id + 1, name + 1, balance2, commission, currency))
    );

    final long playerId = 1;
    final String playerName = "Ronaldo";
    final int playerAge = 35;
    final int playerExperienceMonths = 200;

    Player player = createTestPlayer(playerId, playerName, playerAge, playerExperienceMonths);
    player = playerRepository.save(player);
    final Team team1 = teamRepository.findById(id).orElseThrow();
    team1.getPlayers().add(player);
    teamRepository.save(team1);

    mockMvc.perform(post("/api/v1/player-market/transfer")
        .contentType("application/json")
        .param("from", String.valueOf(1L))
        .param("to", String.valueOf(2))
        .param("playerId", String.valueOf(playerId)))
        .andExpect(status().isOk());

    // when
    final Team result1 = teamRepository.findById(1L).orElseThrow();
    final Set<Player> players1 = result1.getPlayers();
    final Team result2 = teamRepository.findById(2L).orElseThrow();
    final Set<Player> players2 = result2.getPlayers();

    // then
    assertNotNull(result1);
    assertThat(result1.getId()).isEqualTo(id);
    assertThat(result1.getName()).isEqualTo(name);
    assertThat(result1.getBalance()).isEqualTo(BigDecimal.valueOf(balance1));
    assertThat(result1.getCommission()).isEqualTo(BigDecimal.valueOf(commission));
    assertThat(result1.getCurrency()).isEqualTo(currency);
    assertNotNull(players1);
    assertThat(players1.size()).isEqualTo(1);
    assertThat(players1.stream().filter(p -> p.getId().equals(playerId)).findAny().orElseThrow().getName()).isEqualTo(playerName);
    assertThat(players1.stream().filter(p -> p.getId().equals(playerId)).findAny().orElseThrow().getAge()).isEqualTo(playerAge);
    assertThat(players1.stream().filter(p -> p.getId().equals(playerId)).findAny().orElseThrow().getExperienceMonths()).isEqualTo(playerExperienceMonths);

    assertNotNull(result2);
    assertThat(result2.getId()).isEqualTo(id + 1);
    assertThat(result2.getName()).isEqualTo(name + 1);
    assertThat(result2.getBalance()).isEqualTo(BigDecimal.valueOf(balance2));
    assertThat(result2.getCommission()).isEqualTo(BigDecimal.valueOf(commission));
    assertThat(result2.getCurrency()).isEqualTo(currency);
    assertNotNull(players2);
    assertThat(players2.size()).isZero();
  }

  private static Team createTestTeam(Long id, String name, Double balance, Double commission, Currency currency) {
    return new Team(id, name, balance, commission, currency);
  }

  private static Player createTestPlayer(Long id, String name, Integer age, Integer experienceMonths) {
    Player entity = new Player(name, age, experienceMonths);
    entity.setId(id);
    return entity;
  }
}
