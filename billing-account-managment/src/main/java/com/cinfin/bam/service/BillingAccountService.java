package com.cinfin.bam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.dto.requests.AdditionalNameRequest;
import com.cinfin.bam.dto.requests.AddressRequest;
import com.cinfin.bam.dto.requests.PayorRequest;

@Service
public class BillingAccountService {

  private final AssureService assureService;

  private final PayorService payorService;

  @Autowired
  public BillingAccountService(AssureService assureService, PayorService payorService) {
    this.assureService = assureService;
    this.payorService = payorService;
  }

  public void createBillingAccount(AccountBillDTO request) {
    try {
      // Call Assure to see if the account exists
      String accountId =
          this.assureService.checkAccountExists(request.getAccountInfo().getCurrentAccountNbr());

      if (!accountId.isBlank()) {
        // Account already exists, no need to create
        return;
      }
      // Call Assure to see if the payor exists
      // PartySearchItem payorSearchResponse = this.assureService.getPayor(request);

      String guid = "49809f8e-a9e2-4cd2-9968-24550add1c2e";

      String compressedGuid = guid.replace("-", "");
      PayorRequest payorRequest = this.payorService.createPayorRequest(request, compressedGuid);
      String partyId = this.payorService.createPayor(payorRequest);

      if (request.getPayorInfo().getSecondaryName() != null) { // chk additional name is secondary
                                                               // or not
        AdditionalNameRequest additionalNameRequest = new AdditionalNameRequest();
        additionalNameRequest.setType("Doing Business As");
        additionalNameRequest.setFullName(request.getPayorInfo().getSecondaryName());
        this.payorService.createAdditionalName(partyId, additionalNameRequest);
      }

      AddressRequest addressRequest = this.payorService.createNewPayorAddressRequest(request);
      this.payorService.createAddress(partyId, addressRequest);



    } catch (Exception e) {
      // Handle Assure service exceptions
      System.err.println("Error in billing account creation: " + e.getMessage());
    }
  }
}
