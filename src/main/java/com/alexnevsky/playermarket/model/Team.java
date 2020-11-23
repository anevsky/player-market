package com.alexnevsky.playermarket.model;

import com.google.common.collect.Sets;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "team")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "players")
@SequenceGenerator(name = "team_seq", sequenceName = "team_seq", allocationSize = 1)
public class Team {

  @Id
  @NotNull
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_seq")
  @Column(updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  private Long id;

  @NotNull
  private String name;

  private BigDecimal balance = BigDecimal.ZERO;
  private BigDecimal commission = BigDecimal.valueOf(0.075);
  private Currency currency = Currency.getInstance("USD");

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "team_player",
      joinColumns = {@JoinColumn(name = "player_id")},
      inverseJoinColumns = {@JoinColumn(name = "team_id")}
  )
  @BatchSize(size = 12)
  private Set<Player> players = Sets.newHashSet();

  public Team(@NotNull String name) {
    this.name = name;
  }

  public Team(@NotNull Long id, @NotNull String name, Double balance, Double commission, Currency currency) {
    this.id = id;
    this.name = name;
    this.balance = BigDecimal.valueOf(balance);
    this.commission = BigDecimal.valueOf(commission);
    this.currency = currency;
  }
}
