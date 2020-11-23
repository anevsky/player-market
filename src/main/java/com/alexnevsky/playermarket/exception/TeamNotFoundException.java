package com.alexnevsky.playermarket.exception;

/**
 * @author Alex Nevsky
 *
 * Date: 18/11/2020
 */
public class TeamNotFoundException extends RuntimeException {

  public TeamNotFoundException(Long id) {
    super("Could not find team " + id);
  }

}
