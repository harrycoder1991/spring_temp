package com.cinfin.bam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.dto.responses.PartySearchItem;

@Service
public class BillingAccountService {

  private final AssureService assureService;

  @Autowired
  public BillingAccountService(AssureService assureService) {
    this.assureService = assureService;
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
      PartySearchItem payorSearchResponse = this.assureService.getPayor(request);

      if (payorSearchResponse != null) {
        // Payor exists, use the existing payor to create the account
        /*
         * AccountCreationRequest accountCreationRequest = new AccountCreationRequest(payorInfo,
         * payor); this.assureService.createAccount(request); } else { // Payor does not exist,
         * create a new payor PayorInfo newPayor = this.assureService.createPayor(payorInfo);
         * AccountCreationRequest request = new AccountCreationRequest(payorInfo, newPayor);
         * this.assureService.createAccount(request);
         */
      }
    } catch (Exception e) {
      // Handle Assure service exceptions
      System.err.println("Error in billing account creation: " + e.getMessage());
    }
  }
}
