package com.alexnevsky.playermarket;

import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

/**
 * @author Alex Nevsky
 *
 * Date: 18/11/2020
 */
@SpringBootApplication
public class Boot {

  private static final Logger log = LoggerFactory.getLogger(Boot.class);

  @Value("${app.property.welcome.message:hi}")
  private String helloMessage;

  public static void main(String[] args) {
    SpringApplication.run(Boot.class, args);
  }

  @Bean
  CommandLineRunner demo() {
    return args -> {
      log.info("App started.");
      log.info(helloMessage);
    };
  }

  @EventListener(ApplicationReadyEvent.class)
  public void doSomethingAfterAppIsReady() {
    log.info("######################## [o_o] ########################");
    log.info("Hello World, I'm ready to serve!");
    log.info("######################## ##### ########################");
  }

  @PreDestroy
  public void shutDown() {
    log.info("Shutting down service...");
  }
}
