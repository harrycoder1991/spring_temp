package com.cinfin.bam.dto.requests;

import javax.validation.Valid;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "AccountBill")
public class AccountBillDTO {

  @Valid
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "AccountInfo")
  private AccountInfo accountInfo;
  @Valid
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "Policy")
  private Policy policy;
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "PolicyFound")
  private String policyFound;
  @Valid
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "BankInfo")
  private BankInfo bankInfo;
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "PayorInfo")
  private Payor payorInfo;
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "ReturnStatus")
  private String returnStatus;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "AssociatedPolicies")
  private AssociatedPolicies associatedPolicies;

}
