package com.alexnevsky.playermarket.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alexnevsky.playermarket.dto.TeamDto;
import com.alexnevsky.playermarket.model.Player;
import com.alexnevsky.playermarket.model.Team;
import com.alexnevsky.playermarket.repository.PlayerRepository;
import com.alexnevsky.playermarket.repository.TeamRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
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
class TeamControllerTests {

  @Autowired
  private TeamController controller;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TeamRepository repository;

  @Autowired
  private PlayerRepository playerRepository;

  @Test
  void contextLoads() {
    assertThat(controller).isNotNull();
  }

  @Test
  void test_getAll_thenOk() throws Exception {
    // given
    final long id = 1;
    final String name = "Real";
    final double balance = 100;
    final double commission = 0.075;
    final Currency currency = Currency.getInstance("USD");

    repository.saveAll(List.of(
        createTestEntity(id, name, balance, commission, currency),
        createTestEntity(id + 1, name + 1, balance, commission, currency))
    );

    mockMvc.perform(get("/api/v1/teams")
        .contentType("application/json"))
        .andExpect(status().isOk());

    // when
    final List<Team> result = repository.findAll();

    // then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertThat(result.stream().filter(team -> team.getId().equals(id)).findAny().orElseThrow().getName()).isEqualTo(name);
    assertThat(result.stream().filter(team -> team.getId().equals(id)).findAny().orElseThrow().getBalance()).isEqualTo(BigDecimal.valueOf(balance));
    assertThat(result.stream().filter(team -> team.getId().equals(id)).findAny().orElseThrow().getCommission()).isEqualTo(BigDecimal.valueOf(commission));
    assertThat(result.stream().filter(team -> team.getId().equals(id)).findAny().orElseThrow().getCurrency()).isEqualTo(currency);
    assertThat(result.stream().filter(team -> team.getId().equals(id + 1)).findAny().orElseThrow().getName()).isEqualTo(name + 1);
    assertThat(result.stream().filter(team -> team.getId().equals(id + 1)).findAny().orElseThrow().getBalance()).isEqualTo(BigDecimal.valueOf(balance));
    assertThat(result.stream().filter(team -> team.getId().equals(id + 1)).findAny().orElseThrow().getCommission()).isEqualTo(BigDecimal.valueOf(commission));
    assertThat(result.stream().filter(team -> team.getId().equals(id + 1)).findAny().orElseThrow().getCurrency()).isEqualTo(currency);
  }

  @Test
  void test_createTeam_thenOk() throws Exception {
    // given
    final String name = "Milan";
    final double balance = 100;
    final double commission = 0.075;
    final Currency currency = Currency.getInstance("USD");

    TeamDto payload = new TeamDto(null, name, balance, commission, currency, null);

    mockMvc.perform(post("/api/v1/teams")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(payload)))
        .andExpect(status().isOk());

    // when
    final List<Team> result = repository.findAll();

    // then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertThat(result.get(0).getName()).isEqualTo(name);
    assertThat(result.get(0).getBalance()).isEqualTo(BigDecimal.valueOf(balance));
    assertThat(result.get(0).getCommission()).isEqualTo(BigDecimal.valueOf(commission));
    assertThat(result.get(0).getCurrency()).isEqualTo(currency);
  }

  @Test
  void test_getTeam_thenOk() throws Exception {
    // given
    final long id = 1;
    final String name = "Real";
    final double balance = 100;
    final double commission = 0.075;
    final Currency currency = Currency.getInstance("USD");

    repository.saveAll(List.of(createTestEntity(id, name, balance, commission, currency)));

    mockMvc.perform(get("/api/v1/teams/1")
        .contentType("application/json"))
        .andExpect(status().isOk());

    // when
    final Team result = repository.findById(id).orElseThrow();

    // then
    assertNotNull(result);
    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getName()).isEqualTo(name);
    assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(balance));
    assertThat(result.getCommission()).isEqualTo(BigDecimal.valueOf(commission));
    assertThat(result.getCurrency()).isEqualTo(currency);
  }

  @Test
  void test_getTeam_thenFail_becauseNotFound() throws Exception {
    // given
    mockMvc.perform(get("/api/v1/teams/1")
        .contentType("application/json"))
        .andExpect(status().isNotFound());
  }

  @Test
  void test_updateTeam_thenOk() throws Exception {
    // given
    final long id = 1;
    final String name = "Real";
    final String newName = "Real 123";
    final double balance = 100;
    final double newBalance = 2200;
    final double commission = 0.075;
    final Currency currency = Currency.getInstance("USD");

    repository.saveAll(List.of(
        createTestEntity(id, name, balance, commission, currency),
        createTestEntity(id + 1, name + 1, balance, commission, currency))
    );

    TeamDto payload = new TeamDto();
    payload.setId(id);
    payload.setName(newName);
    payload.setBalance(newBalance);

    mockMvc.perform(put("/api/v1/teams/1")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(payload)))
        .andExpect(status().isOk());

    // when
    final Team result = repository.findById(id).orElseThrow();

    // then
    assertNotNull(result);
    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getName()).isEqualTo(newName);
    assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(newBalance));
    assertThat(result.getCommission()).isEqualTo(BigDecimal.valueOf(commission));
    assertThat(result.getCurrency()).isEqualTo(currency);
  }

  @Test
  void test_deleteTeam_thenOk() throws Exception {
    // given
    final long id = 1;
    final String name = "Real";
    final double balance = 100;
    final double commission = 0.075;
    final Currency currency = Currency.getInstance("USD");

    repository.saveAll(List.of(
        createTestEntity(id, name, balance, commission, currency),
        createTestEntity(id + 1, name + 1, balance, commission, currency))
    );

    final List<Team> saved = repository.findAll();
    assertNotNull(saved);
    assertEquals(2, saved.size());

    mockMvc.perform(delete("/api/v1/teams/1")
        .contentType("application/json"))
        .andExpect(status().isOk());

    // when
    final Optional<Team> result = repository.findById(id);

    // then
    assertTrue(result.isEmpty());
  }

  @Test
  void test_addPlayer_thenOk() throws Exception {
    // given
    final long id = 1;
    final String name = "Real";
    final double balance = 100;
    final double commission = 0.075;
    final Currency currency = Currency.getInstance("USD");

    repository.saveAll(List.of(
        createTestEntity(id, name, balance, commission, currency),
        createTestEntity(id + 1, name + 1, balance, commission, currency))
    );

    final long playerId = 1;
    final String playerName = "Ronaldo";
    final int playerAge = 35;
    final int playerExperienceMonths = 200;

    Player player = createTestPlayer(playerId, playerName, playerAge, playerExperienceMonths);

    playerRepository.save(player);

    mockMvc.perform(patch("/api/v1/teams/1/player")
        .contentType("application/json")
        .param("id", String.valueOf(playerId)))
        .andExpect(status().isOk());

    // when
    final Team result = repository.findById(id).orElseThrow();

    final Set<Player> players = result.getPlayers();

    // then
    assertNotNull(result);
    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getName()).isEqualTo(name);
    assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(balance));
    assertThat(result.getCommission()).isEqualTo(BigDecimal.valueOf(commission));
    assertThat(result.getCurrency()).isEqualTo(currency);
    assertNotNull(players);
    assertThat(players.size()).isEqualTo(1);
    assertThat(players.stream().filter(p -> p.getId().equals(playerId)).findAny().orElseThrow().getName()).isEqualTo(playerName);
    assertThat(players.stream().filter(p -> p.getId().equals(playerId)).findAny().orElseThrow().getAge()).isEqualTo(playerAge);
    assertThat(players.stream().filter(p -> p.getId().equals(playerId)).findAny().orElseThrow().getExperienceMonths()).isEqualTo(playerExperienceMonths);
  }

  @Test
  void test_addMoney_thenOk() throws Exception {
    // given
    final long id = 1;
    final String name = "Real";
    final double balance = 0;
    final double newBalance = 2200;
    final double commission = 0.075;
    final Currency currency = Currency.getInstance("USD");

    repository.saveAll(List.of(
        createTestEntity(id, name, balance, commission, currency),
        createTestEntity(id + 1, name + 1, balance, commission, currency))
    );

    mockMvc.perform(patch("/api/v1/teams/1/balance")
        .contentType("application/json")
        .param("amount", String.valueOf(newBalance)))
        .andExpect(status().isOk());

    // when
    final Team result = repository.findById(id).orElseThrow();

    // then
    assertNotNull(result);
    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getName()).isEqualTo(name);
    assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(newBalance));
    assertThat(result.getCommission()).isEqualTo(BigDecimal.valueOf(commission));
    assertThat(result.getCurrency()).isEqualTo(currency);
  }

  private static Team createTestEntity(Long id, String name, Double balance, Double commission, Currency currency) {
    return new Team(id, name, balance, commission, currency);
  }

  private static Player createTestPlayer(Long id, String name, Integer age, Integer experienceMonths) {
    Player entity = new Player(name, age, experienceMonths);
    entity.setId(id);
    return entity;
  }
}
