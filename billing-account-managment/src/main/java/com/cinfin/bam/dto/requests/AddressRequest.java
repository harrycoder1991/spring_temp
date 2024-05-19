package com.cinfin.bam.dto.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressRequest {

  @JsonProperty("addressType")
  private String addressType;

  @JsonProperty("addressLine1")
  private String addressLine1;

  @JsonProperty("addressLine2")
  private String addressLine2;

  @JsonProperty("attentionLine")
  private String attentionLine;

  @JsonProperty("city")
  private String city;

  @JsonProperty("postal")
  private String postal;

  @JsonProperty("state")
  private String state;

  @JsonProperty("country")
  private String country;
}
