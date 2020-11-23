package com.alexnevsky.playermarket.exception;

/**
 * @author Alex Nevsky
 *
 * Date: 18/11/2020
 */
public class PlayerNotFoundException extends RuntimeException {

  public PlayerNotFoundException(Long id) {
    super("Could not find player " + id);
  }

}
