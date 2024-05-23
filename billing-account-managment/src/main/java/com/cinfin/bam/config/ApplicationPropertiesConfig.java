package com.cinfin.bam.config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@Data
public class ApplicationPropertiesConfig {

  @Value("${assure.api.createpayor.endpoint}")
  private String endpoint;

  @Value("${assure.api.url}")
  private String url;



}
