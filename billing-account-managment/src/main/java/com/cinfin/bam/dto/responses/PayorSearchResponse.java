package com.cinfin.bam.dto.responses;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayorSearchResponse {

  @Data
  public static class Address {
    private String addressType;
    private String tag;
    private String address;
    private int addressSequence;
    private String attentionLine;
  }

  @Data
  public static class Embedded {
    @JsonProperty("partySearchList")
    private List<PartySearchItem> partySearchList;
  }

  @Data
  public static class Item {
    private String href;
  }

  @Data
  public static class Link {
    private String href;
  }

  @Data
  public static class Links {
    private List<Item> item;
    private Link current;
    private Link first;
    private Link last;
    private Link self;
  }

  @Data
  public static class PartySearchItem {
    private String partyId;
    private String name;
    private String type;
    private String externalId;
    private String createdOn;
    private String pictureId;
    private String timestamp;
    private List<Address> addresses;
    private Links _links;
  }

  @JsonProperty("_embedded")
  private Embedded embedded;

  private Links _links;
}
