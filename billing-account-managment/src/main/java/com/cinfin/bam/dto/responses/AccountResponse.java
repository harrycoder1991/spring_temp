package com.cinfin.bam.dto.responses;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Embedded {

    @JsonProperty("accountSearchList")
    private List<Account> accountSearchList;

    public List<Account> getAccountSearchList() {
      return this.accountSearchList;
    }

    public void setAccountSearchList(List<Account> accountSearchList) {
      this.accountSearchList = accountSearchList;
    }
  }

  @JsonProperty("_embedded")
  private Embedded embedded;

  public Embedded getEmbedded() {
    return this.embedded;
  }

  public void setEmbedded(Embedded embedded) {
    this.embedded = embedded;
  }
}
