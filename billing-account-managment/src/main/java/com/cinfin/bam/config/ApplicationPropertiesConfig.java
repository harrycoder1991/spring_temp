package com.cinfin.bam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import lombok.Data;

@Configuration
@Data
@PropertySource("classpath: env.properties")
public class ApplicationPropertiesConfig {
  @Value("http://mock:8080")
  private String assureBaseUrl;
  @Value("/parties/v1/parties")
  private String assureCreatePayorEndpoint;
  @Value("jharve4")
  private String assureServiceHeaderUserId;
  @Value("False")
  private String assureServiceHeaderRequest;
}
