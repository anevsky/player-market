package com.alexnevsky.playermarket.mapper;

import com.alexnevsky.playermarket.dto.PlayerDto;
import com.alexnevsky.playermarket.dto.TeamDto;
import com.alexnevsky.playermarket.model.Player;
import com.alexnevsky.playermarket.model.Team;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author Alex Nevsky
 *
 * Date: 18/11/2020
 */
@Mapper(componentModel = "spring")
public interface PlayerMarketMapper {

  // player

  @Mapping(target = "teams", ignore = true )
  PlayerDto toPlayerDto(Player source);
  @Mapping(target = "teams", ignore = true )
  Player toPlayerEntity(PlayerDto source);
  List<PlayerDto> toPlayerDtoCollection(List<Player> all);
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "teams", ignore = true)
  default Player updatePlayerFromDto(@MappingTarget Player target, PlayerDto source) {
    if (source == null) {
      return null;
    }

    if (source.getName() != null) {
      target.setName(source.getName());
    }
    if (source.getAge() != null) {
      target.setAge(source.getAge());
    }
    if (source.getExperienceMonths() != null) {
      target.setExperienceMonths( source.getExperienceMonths());
    }

    return target;
  }
  @AfterMapping
  default void injectTeamsIds(@MappingTarget PlayerDto target, Player source) {
    target.setTeams(source.getTeams().stream().map(Team::getId).collect(Collectors.toSet()));
  }

  // team

  TeamDto toTeamDto(Team source);
  Team toTeamEntity(TeamDto source);
  List<TeamDto> toTeamDtoCollection(List<Team> all);
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "players", ignore = true)
  default Team updateTeamFromDto(@MappingTarget Team target, TeamDto source) {
    if (source == null) {
      return null;
    }

    if (source.getName() != null) {
      target.setName( source.getName() );
    }
    if (source.getBalance() != null) {
      target.setBalance(BigDecimal.valueOf(source.getBalance()));
    }
    if (source.getCommission() != null) {
      target.setCommission(BigDecimal.valueOf(source.getCommission()));
    }
    if (source.getCurrency() != null) {
      target.setCurrency(source.getCurrency());
    }

    return target;
  }
}
