package com.alexnevsky.playermarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Alex Nevsky
 *
 * Date: 19/11/2020
 */
@ControllerAdvice
public class PlayerNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(PlayerNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String notFoundHandler(PlayerNotFoundException ex) {
    return ex.getMessage();
  }
}
