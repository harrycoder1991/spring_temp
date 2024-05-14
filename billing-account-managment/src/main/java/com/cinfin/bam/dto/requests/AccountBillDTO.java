package com.cinfin.bam.dto.requests;
import lombok.Data;


public class AccountBillDTO {
    private AccountInfoDTO accountInfo;
    private PolicyDTO policy;
    private String policyFound;
    private BankInfoDTO bankInfo;
    private PayorDTO payorInfo;
    private String returnStatus;
}
