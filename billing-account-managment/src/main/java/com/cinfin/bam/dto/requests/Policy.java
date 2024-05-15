package com.cinfin.bam.dto.requests;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Policy {
  @JacksonXmlProperty(localName = "SystemId")
  private String systemId;

  @JacksonXmlProperty(localName = "QuoteNbr")
  private String quoteNbr;

  @JacksonXmlProperty(localName = "PolicyNumber")
  private String policyNumber;

  @JacksonXmlProperty(localName = "PolicyPrefix")
  private String policyPrefix;

  @JacksonXmlProperty(localName = "AgencyId")
  private String agencyId;

  @JacksonXmlProperty(localName = "EffectiveDt")
  private String effectiveDt;

  @JacksonXmlProperty(localName = "ExpirationDt")
  private String expirationDt;

  @JacksonXmlProperty(localName = "UserId")
  private String userId;

  @JacksonXmlProperty(localName = "PaymentPlanCd")
  private String paymentPlanCd;

  @JacksonXmlProperty(localName = "SubmissionNbr")
  private String submissionNbr;

}
