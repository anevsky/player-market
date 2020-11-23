package com.alexnevsky.playermarket.controller;

import com.alexnevsky.playermarket.dto.TransferEvent;
import com.alexnevsky.playermarket.service.PlayerTransferService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alex Nevsky
 *
 * Date: 19/11/2020
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/player-market")
public class MarketController {

  private final PlayerTransferService playerTransferService;

  @PostMapping("/transfer")
  public TransferEvent transfer(@RequestParam Long from, @RequestParam Long to, @RequestParam Long playerId) {
    return playerTransferService.transfer(from, to, playerId);
  }
}
