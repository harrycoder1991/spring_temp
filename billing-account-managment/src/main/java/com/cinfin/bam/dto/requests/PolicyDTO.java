package com.cinfin.bam.dto.requests;

import lombok.Data;

@Data
public class PolicyDTO {
    private String systemId;
    private String quoteNbr;
    private String policyNumber;
    private String policyPrefix;
    private String agencyId;
    private String effectiveDt;
    private String expirationDt;
    private String userId;
    private String paymentPlanCd;
    private String submissionNbr;
}