package com.veamospues.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by alejandro on 19/11/16.
 */
@Configuration
public class BarConfiguration {

  @Bean(name = "bar")
  public String bar() {
    return "bar";
  }
}
