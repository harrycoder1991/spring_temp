package com.cinfin.bam.dto.requests;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
  @JacksonXmlProperty(localName = "CurrentAccountNbr")
  private String currentAccountNbr;

  @JacksonXmlProperty(localName = "MethodOfPayment", isAttribute = true)
  private String methodOfPayment;

  @JacksonXmlProperty(localName = "AuditMOP", isAttribute = true)
  private String auditMOP;

  @JacksonXmlProperty(localName = "InvoiceDueDay", isAttribute = true)
  private int invoiceDueDay;

  @JacksonXmlProperty(localName = "AutopayInd")
  private String autopayInd;

  @JacksonXmlProperty(localName = "PartyTransInd")
  private String partyTransInd;

  @JacksonXmlProperty(localName = "AutopayAllowedInd")
  private String autopayAllowedInd;
}
