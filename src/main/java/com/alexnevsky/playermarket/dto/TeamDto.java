package com.alexnevsky.playermarket.dto;

import java.util.Currency;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Alex Nevsky
 *
 * Date: 19/11/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "players")
public class TeamDto {

  @EqualsAndHashCode.Include
  private Long id;
  @NotNull
  @EqualsAndHashCode.Include
  private String name;
  private Double balance;
  private Double commission;
  private Currency currency;
  private Set<PlayerDto> players;
}
