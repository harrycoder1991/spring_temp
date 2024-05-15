package com.cinfin.bam.dto.requests;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociatedPolicies {

  @JacksonXmlProperty(localName = "PolicyOccurence")
  private PolicyOccurence policyOccurence;



}
