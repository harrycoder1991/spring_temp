package com.cinfin.bam.dto.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayorRequest {

  @Data
  public static class Details {
    @Data
    public static class Name {
      @JsonProperty("given")
      private String given;

      @JsonProperty("middle")
      private String middle;

      @JsonProperty("family")
      private String family;

      @JsonProperty("prefix")
      private String prefix;

      @JsonProperty("suffix")
      private String suffix;
    }

    @JsonProperty("name")
    private Name name;
  }

  @Data
  public static class Email {
    @JsonProperty("primary")
    private String primary;
  }

  @Data
  public static class Phone {
    @Data
    public static class Primary {
      @JsonProperty("number")
      private String number;
    }

    @JsonProperty("primary")
    private Primary primary;
  }

  @JsonProperty("isPerson")
  private boolean isPerson;

  @JsonProperty("details")
  private Details details;

  @JsonProperty("externalId")
  private String externalId;

  @JsonProperty("email")
  private Email email;

  @JsonProperty("phone")
  private Phone phone;
}
