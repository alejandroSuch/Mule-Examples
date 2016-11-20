package com.veamospues.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by alejandro on 19/11/16.
 */
@Configuration
@ComponentScan(basePackages = {"com.veamospues.services"})
public class FooConfiguration {

  @Bean(name = "foo")
  public String getFoo() {
    return "foo";
  }
}
