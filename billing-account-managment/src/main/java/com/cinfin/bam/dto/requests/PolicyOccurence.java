package com.cinfin.bam.dto.requests;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyOccurence {

  @JacksonXmlProperty(localName = "PolicyPrefix")
  private String policyPrefix;

  @JacksonXmlProperty(localName = "PolicyNumber")
  private String policyNumber;

  @JacksonXmlProperty(localName = "PaymentPlanCd")
  private String paymentPlanCd;


}
