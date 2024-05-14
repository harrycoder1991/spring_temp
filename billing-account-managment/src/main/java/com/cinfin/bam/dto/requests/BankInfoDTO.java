package com.cinfin.bam.dto.requests;
import lombok.Data;

@Data
public class BankInfoDTO {
    private String bankName;
    private String bankId;
    private String bankAcctType;
    private String eftDeductionDay;
}
