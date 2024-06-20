package com.cinfin.bam.dto.requests;

import java.time.LocalDate;
import lombok.Data;

@Data
public class AccountCreationRequest {
  private String accountNumber;
  private String type;
  private String billingType;
  private String accountPlan;
  private LocalDate accountDueDate;
  private LocalDate billThroughDate;
  private String presentmentMethod;
  private String payorId;
  private String payorAddressId;

}
