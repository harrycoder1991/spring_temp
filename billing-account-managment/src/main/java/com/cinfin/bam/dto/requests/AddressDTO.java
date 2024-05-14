package com.cinfin.bam.dto.requests;

import lombok.Data;

@Data
public class AddressDTO {
    private String city;
    private String county;
    private String stateProvCd;
    private String country;
    private String addr1;
    private String addr2;
    private String zip;
    private String zipExtension;
    private String addressVerified;
    private String addressOverride;
}
