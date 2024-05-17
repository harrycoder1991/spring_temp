package com.cinfin.bam.dto.responses;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayorSearchResponse {

  @Data
  public static class Embedded {
    @JsonProperty("partySearchList")
    private List<PartySearchItem> partySearchList;
  }

  @JsonProperty("_embedded")
  private Embedded embedded;
}
