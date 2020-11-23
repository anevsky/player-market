package com.alexnevsky.playermarket.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alexnevsky.playermarket.dto.PlayerDto;
import com.alexnevsky.playermarket.model.Player;
import com.alexnevsky.playermarket.repository.PlayerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
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
class PlayerControllerTests {

  @Autowired
  private PlayerController controller;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PlayerRepository repository;

  @Test
  void contextLoads() {
    assertThat(controller).isNotNull();
  }

  @Test
  void test_getAll_thenOk() throws Exception {
    // given
    final long id = 1;
    final String name = "Ronaldo";
    final int age = 35;
    final int experienceMonths = 200;

    repository.saveAll(List.of(
        createTestEntity(id, name, age, experienceMonths),
        createTestEntity(id + 1, name, age, experienceMonths))
    );

    mockMvc.perform(get("/api/v1/players")
        .contentType("application/json"))
        .andExpect(status().isOk());

    // when
    final List<Player> result = repository.findAll();

    // then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertThat(result.stream().filter(player -> player.getId().equals(id)).findAny().orElseThrow().getName()).isEqualTo(name);
    assertThat(result.stream().filter(player -> player.getId().equals(id)).findAny().orElseThrow().getAge()).isEqualTo(age);
    assertThat(result.stream().filter(player -> player.getId().equals(id)).findAny().orElseThrow().getExperienceMonths()).isEqualTo(experienceMonths);
    assertThat(result.stream().filter(player -> player.getId().equals(id + 1)).findAny().orElseThrow().getName()).isEqualTo(name);
    assertThat(result.stream().filter(player -> player.getId().equals(id + 1)).findAny().orElseThrow().getAge()).isEqualTo(age);
    assertThat(result.stream().filter(player -> player.getId().equals(id + 1)).findAny().orElseThrow().getExperienceMonths()).isEqualTo(experienceMonths);
  }

  @Test
  void test_createPlayer_thenOk() throws Exception {
    // given
    final String name = "Rafa";
    final int age = 35;
    final int experienceMonths = 200;

    PlayerDto payload = new PlayerDto(null, name, age, experienceMonths, null);

    mockMvc.perform(post("/api/v1/players")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(payload)))
        .andExpect(status().isOk());

    // when
    final List<Player> result = repository.findAll();

    // then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertThat(result.get(0).getName()).isEqualTo(name);
    assertThat(result.get(0).getAge()).isEqualTo(age);
    assertThat(result.get(0).getExperienceMonths()).isEqualTo(experienceMonths);
  }

  @Test
  void test_getPlayer_thenOk() throws Exception {
    // given
    final long id = 1;
    final String name = "Ronaldo";
    final int age = 35;
    final int experienceMonths = 200;

    repository.saveAll(List.of(createTestEntity(id, name, age, experienceMonths)));

    mockMvc.perform(get("/api/v1/players/1")
        .contentType("application/json"))
        .andExpect(status().isOk());

    // when
    final Player result = repository.findById(id).orElseThrow();

    // then
    assertNotNull(result);
    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getName()).isEqualTo(name);
    assertThat(result.getAge()).isEqualTo(age);
    assertThat(result.getExperienceMonths()).isEqualTo(experienceMonths);
  }

  @Test
  void test_getPlayer_thenFail_becauseNotFound() throws Exception {
    // given
    mockMvc.perform(get("/api/v1/players/1")
        .contentType("application/json"))
        .andExpect(status().isNotFound());
  }

  @Test
  void test_updatePlayer_thenOk() throws Exception {
    // given
    final long id = 1;
    final String name = "Ronaldo";
    final String newName = "Ronaldo 123";
    final int age = 35;
    final int newAge = 37;
    final int experienceMonths = 200;

    repository.saveAll(List.of(
        createTestEntity(id, name, age, experienceMonths),
        createTestEntity(id + 1, name, age, experienceMonths))
    );

    PlayerDto payload = new PlayerDto(id, newName, newAge, null, null);

    mockMvc.perform(put("/api/v1/players/1")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(payload)))
        .andExpect(status().isOk());

    // when
    final Player result = repository.findById(id).orElseThrow();

    // then
    assertNotNull(result);
    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getName()).isEqualTo(newName);
    assertThat(result.getAge()).isEqualTo(newAge);
    assertThat(result.getExperienceMonths()).isEqualTo(experienceMonths);
  }

  @Test
  void test_deletePlayer_thenOk() throws Exception {
    // given
    final long id = 1;
    final String name = "Ronaldo";
    final int age = 35;
    final int experienceMonths = 200;

    repository.saveAll(List.of(
        createTestEntity(id, name, age, experienceMonths),
        createTestEntity(id + 1, name, age, experienceMonths))
    );

    final List<Player> saved = repository.findAll();
    assertNotNull(saved);
    assertEquals(2, saved.size());

    mockMvc.perform(delete("/api/v1/players/1")
        .contentType("application/json"))
        .andExpect(status().isOk());

    // when
    final Optional<Player> result = repository.findById(id);

    // then
    assertTrue(result.isEmpty());
  }

  private static Player createTestEntity(Long id, String name, Integer age, Integer experienceMonths) {
    Player entity = new Player(name, age, experienceMonths);
    entity.setId(id);
    return entity;
  }
}
