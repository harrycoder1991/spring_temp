package com.cinfin.bam.dto.requests;



import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayorAddress {

  @JacksonXmlProperty(localName = "City", isAttribute = true)
  private String city;

  @JacksonXmlProperty(localName = "County", isAttribute = true)
  private String county;

  @JacksonXmlProperty(localName = "StateProvCd", isAttribute = true)
  private String stateProvCd;

  @JacksonXmlProperty(localName = "Country", isAttribute = true)
  private String country;

  @JacksonXmlProperty(localName = "Addr1", isAttribute = true)
  private String addr1;

  @JacksonXmlProperty(localName = "Addr2")
  private String addr2;

  @JacksonXmlProperty(localName = "Zip", isAttribute = true)
  private String zip;

  @JacksonXmlProperty(localName = "ZipExtension")
  private String zipExtension;

  @JacksonXmlProperty(localName = "PostalCode")
  private String postalCode;

  @JacksonXmlProperty(localName = "AddressVerified")
  private String addressVerified;

  @JacksonXmlProperty(localName = "AddressOverride")
  private String addressOverride;
}
