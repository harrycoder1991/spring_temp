package com.cinfin.bam.dto.requests;

import lombok.Data;

@Data
public class AccountInfoDTO {
    private String currentAccountNbr;
    private String methodOfPayment;
    private String auditMOP;
    private String invoiceDueDay;
    private String autopayInd;
    private String partyTransInd;
    private String autopayAllowedInd;

}