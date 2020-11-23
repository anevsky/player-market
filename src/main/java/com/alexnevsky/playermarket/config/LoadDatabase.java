package com.alexnevsky.playermarket.config;

import com.alexnevsky.playermarket.model.Player;
import com.alexnevsky.playermarket.model.Team;
import com.alexnevsky.playermarket.repository.PlayerRepository;
import com.alexnevsky.playermarket.repository.TeamRepository;
import com.google.common.collect.Lists;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Alex Nevsky
 *
 * Date: 18/11/2020
 */
@Profile("dev")
@Configuration
public class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(PlayerRepository playerRepository, TeamRepository teamRepository) {
    return args -> {
      List<Player> players = Lists.newArrayList(
          new Player("Ronaldo", 32, 240),
          new Player("Zidane", 47, 420),
          new Player("Ramos", 37, 331),
          new Player("Hernandez", 27, 190),
          new Player("Sychev", 34, 225)
      );
      players = playerRepository.saveAll(players);
      log.info("Preloading {}", players);

      List<Team> teams = Lists.newArrayList(
          new Team("Real"),
          new Team("Milan"),
          new Team("Portugal")
      );
      teams = teamRepository.saveAll(teams);
      teams.get(0).getPlayers().addAll(players.subList(0, 3));
      teams.get(1).getPlayers().addAll(players.subList(3, 4));
      teams = teamRepository.saveAll(teams);
      log.info("Preloading {}", teams);
    };
  }
}
