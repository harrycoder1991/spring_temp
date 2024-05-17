package com.cinfin.bam.dto.requests;

public class AccountCreationRequest {
  private String accountNumber;
  private String type;
  private String billingType;
  private String accountPlan;
  private String accountDueDate;
  private String billThroughDate;
  private String presentmentMethod;
  private String payorId;
  private int payorAddressId;

  /*
   * public AccountCreationRequest(AccountPayorInfo payorInfo, PayorInfo payor) { this.accountNumber
   * = payorInfo.getCurrentAccountNbr(); this.type = "DirectBill"; this.billingType =
   * "Account Billing"; this.accountPlan = "CI4"; this.accountDueDate = "2024-05-01";
   * this.billThroughDate = "2024-05-01"; this.presentmentMethod = "Paper"; this.payorId =
   * payor.getPayorId(); this.payorAddressId = payor.getPayorAddressId(); }
   */

  // getters and setters
}
