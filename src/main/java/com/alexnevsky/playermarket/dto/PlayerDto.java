package com.alexnevsky.playermarket.dto;

import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Alex Nevsky
 *
 * Date: 19/11/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PlayerDto {

  @EqualsAndHashCode.Include
  private Long id;
  @NotNull
  @EqualsAndHashCode.Include
  private String name;
  private Integer age;
  private Integer experienceMonths;
  private Set<Long> teams;
}
