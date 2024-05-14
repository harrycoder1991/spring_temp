package com.cinfin.bam.dto.requests;

import lombok.Data;


public class PayorDTO {
    private String payorType;
    private String companyName;
    private String titlePrefix;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nameSuffix;
    private String payorPhone;
    private String payorEmail;
    private AddressDTO payorAddress;
}
