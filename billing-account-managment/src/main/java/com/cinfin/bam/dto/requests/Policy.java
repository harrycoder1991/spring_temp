package com.cinfin.bam.dto.requests;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
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
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate effectiveDt;

  @JacksonXmlProperty(localName = "ExpirationDt")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate expirationDt;

  @JacksonXmlProperty(localName = "UserId")
  private String userId;

  @JacksonXmlProperty(localName = "PaymentPlanCd")
  private String paymentPlanCd;

  @JacksonXmlProperty(localName = "SubmissionNbr")
  private String submissionNbr;

}
