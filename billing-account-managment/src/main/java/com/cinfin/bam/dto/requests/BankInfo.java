package com.cinfin.bam.dto.requests;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankInfo {

  @JacksonXmlProperty(localName = "BankName")
  private String bankName;

  @JacksonXmlProperty(localName = "BankId", isAttribute = true)
  @NotNull(message = "BankId is required")
  private String bankId;

  @JacksonXmlProperty(localName = "BankAcctType", isAttribute = true)
  private String bankAcctType;

  @JacksonXmlProperty(localName = "EFTDeductionDay", isAttribute = true)
  private int eftDeductionDay;
}
