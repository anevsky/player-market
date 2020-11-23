package com.alexnevsky.playermarket.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/**
 * @author Alex Nevsky
 *
 * Date: 19/11/2020
 */
@Data
@Builder
public class TransferEvent {

  private UUID id;
  private Long iteration;
  private String player;
  private String sourceTeam;
  private String targetTeam;
  private BigDecimal transferFee;
  private BigDecimal contractFee;
  private String status;
  private Instant time;
}
