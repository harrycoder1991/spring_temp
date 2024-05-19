package com.cinfin.bam.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdditionalNameRequest {
  @JsonProperty("type")
  private String type;

  @JsonProperty("fullName")
  private String fullName;
}
