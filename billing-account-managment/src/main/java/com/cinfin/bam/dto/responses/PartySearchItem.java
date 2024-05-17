package com.cinfin.bam.dto.responses;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PartySearchItem {

  @Data
  public static class AdditionalName {
    @JsonProperty("additionalNameSequence")
    private int additionalNameSequence;

    @JsonProperty("additionalNameType")
    private String additionalNameType;

    @JsonProperty("additionalName")
    private String additionalName;
  }

  @Data
  public static class Address {
    @JsonProperty("addressType")
    private String addressType;

    @JsonProperty("tag")
    private String tag;

    @JsonProperty("address")
    private String address;

    @JsonProperty("addressSequence")
    private int addressSequence;

    @JsonProperty("attentionLine")
    private String attentionLine; // Only for the address in ZEEZ6VNAX0Z55FT6JID0
  }

  @Data
  public static class Link {
    @JsonProperty("href")
    private String href;
  }

  @Data
  public static class Links {
    @JsonProperty("party")
    private Link party;
  }

  @JsonProperty("partyId")
  private String partyId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("type")
  private String type;

  @JsonProperty("agencies")
  private List<String> agencies;

  @JsonProperty("externalId")
  private String externalId;

  @JsonProperty("createdOn")
  private String createdOn;

  @JsonProperty("pictureId")
  private String pictureId;

  @JsonProperty("timestamp")
  private String timestamp;

  @JsonProperty("addresses")
  private List<Address> addresses;

  @JsonProperty("additionalNames")
  private List<AdditionalName> additionalNames;

  @JsonProperty("_links")
  private Links links;
}
