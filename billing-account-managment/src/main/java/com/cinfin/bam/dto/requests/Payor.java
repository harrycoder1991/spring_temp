package com.cinfin.bam.dto.requests;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payor {
  @JacksonXmlProperty(localName = "PayorType", isAttribute = true)
  private String payorType;

  @JacksonXmlProperty(localName = "CompanyName", isAttribute = true)
  private String companyName;

  @JacksonXmlProperty(localName = "TitlePrefix")
  private String titlePrefix;

  @JacksonXmlProperty(localName = "FirstName", isAttribute = true)
  private String firstName;

  @JacksonXmlProperty(localName = "MiddleName")
  private String middleName;

  @JacksonXmlProperty(localName = "LastName", isAttribute = true)
  private String lastName;

  @JacksonXmlProperty(localName = "NameSuffix")
  private String nameSuffix;


  @JacksonXmlProperty(localName = "SecondaryName")
  private String secondaryName;

  @JacksonXmlProperty(localName = "PayorPhoneType")
  private String payorPhoneType;

  @JacksonXmlProperty(localName = "PayorPhone", isAttribute = true)
  private String payorPhone;

  @JacksonXmlProperty(localName = "PayorEmail")
  private String payorEmail;

  @JacksonXmlProperty(localName = "PayorAddress")
  private PayorAddress payorAddress;

}
