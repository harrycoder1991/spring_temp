package com.cinfin.bam.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

  private String accountId;
  private String accountNumber;
  private String type;
  private String payorId;
  private String payorAddressId;
  private String payorName;
  private String payorAddress;
  private String imageId;
  private String status;
  private String billingType;
  private String accountPlan;
  private String paymentMethod;
  private String accountDueDate;
  private String billThroughDate;
  private String presentmentMethod;
  private String activityDate;
  private boolean isRecurring;
  private String frequency;
  private String businessGroup;
  private boolean isAutoPayEnable;
  // private List<Link> links;

  // Getters and setters

  public String getAccountId() {
    return this.accountId;
  }

  public String getAccountNumber() {
    return this.accountNumber;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  // Other properties, getters, and setters...
}
