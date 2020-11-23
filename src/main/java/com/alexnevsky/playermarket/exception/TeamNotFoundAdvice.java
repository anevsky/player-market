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
public class TeamNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(TeamNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String notFoundHandler(TeamNotFoundException ex) {
    return ex.getMessage();
  }
}
