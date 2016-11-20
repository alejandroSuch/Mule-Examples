package com.veamospues.mule.config.builders;


import org.mule.MuleServer;
import org.mule.api.MuleContext;
import org.mule.api.config.ConfigurationException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.config.ConfigResource;
import org.mule.config.builders.AutoConfigurationBuilder;
import org.mule.config.spring.SpringRegistry;
import org.reflections.Reflections;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * Created by alejandro on 19/11/16.
 */
public class SpringAnnotationConfigurationBuilder extends AutoConfigurationBuilder {

  public static final String CONFIGURATIONBUILDER_BASEPACKAGE = "configurationbuilder.basepackage";

  public SpringAnnotationConfigurationBuilder(String[] configResources) throws ConfigurationException {
    super(configResources);
  }

  public SpringAnnotationConfigurationBuilder(String configResources) throws ConfigurationException {
    super(configResources);
  }

  public SpringAnnotationConfigurationBuilder(ConfigResource[] configResources) {
    super(configResources);
  }

  @Override
  public void doConfigure(MuleContext muleContext) throws ConfigurationException {
    String basePackage = getBasePackage();

    if (basePackage != null) {
      final AnnotationConfigApplicationContext ctx = createApplicationContext(basePackage);
      final SpringRegistry springRegistry = new SpringRegistry(ctx, muleContext);

      muleContext.addRegistry(springRegistry);

      try {
        springRegistry.initialise();
      } catch (InitialisationException e) {
        throw new ConfigurationException(e);
      }
    }

    super.doConfigure(muleContext);
  }

  private String getBasePackage() {
    final InputStream source = getClass().getResourceAsStream(File.separator + MuleServer.DEFAULT_APP_CONFIGURATION);

    if (source != null) {
      final Properties props = new Properties();
      try {
        props.load(source);
        return props.getProperty(CONFIGURATIONBUILDER_BASEPACKAGE);
      } catch (IOException e) {
        logger.warn("Could't load mule-app.propreties file");
        logger.warn(e);
      }
    }
    return null;
  }

  private AnnotationConfigApplicationContext createApplicationContext(String basePackage) {
    final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    final Reflections reflections = new Reflections("com.veamospues");
    final Set<Class<?>> configurationClasses = reflections.getTypesAnnotatedWith(Configuration.class);
    final Iterator<Class<?>> it = configurationClasses.iterator();

    while (it.hasNext()) {
      ctx.register(it.next());
    }

    return ctx;
  }
}