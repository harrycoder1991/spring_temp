package com.cinfin.bam.config;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setMessageConverters(Arrays.asList(new ByteArrayHttpMessageConverter(),
        new StringHttpMessageConverter(StandardCharsets.UTF_8), new FormHttpMessageConverter(),
        new MappingJackson2HttpMessageConverter(), new MappingJackson2XmlHttpMessageConverter()));
    return restTemplate;
  }
}
