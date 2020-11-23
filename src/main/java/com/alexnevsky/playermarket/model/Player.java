package com.alexnevsky.playermarket.model;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

/**
 * @author Alex Nevsky
 *
 * Date: 18/11/2020
 */
@Table(name = "player")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "teams")
@SequenceGenerator(name = "player_seq", sequenceName = "player_seq", allocationSize = 1)
public class Player {

  @Id
  @NotNull
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_seq")
  @Column(updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  private Long id;

  @NotNull
  private String name;

  private Integer age;

  private Integer experienceMonths = 0;

  @ManyToMany(mappedBy = "players")
  @BatchSize(size = 2)
  private Set<Team> teams = Sets.newHashSet();

  public Player(@NotNull String name, Integer age, Integer experienceMonths) {
    this.name = name;
    this.age = age;
    this.experienceMonths = experienceMonths;
  }
}
