package com.cinfin.bam.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.dto.requests.AccountCreationRequest;
import com.cinfin.bam.dto.requests.AdditionalNameRequest;
import com.cinfin.bam.dto.requests.AddressRequest;
import com.cinfin.bam.dto.requests.PayorRequest;
import com.cinfin.bam.entity.DirectBillPlan;
import com.cinfin.bam.entity.PartyClientXref;
import com.cinfin.bam.repository.DirectBillPlanRepository;
import com.cinfin.bam.repository.PartyClientXrefRepository;

@Service
public class BillingAccountService {


  private final AssureService assureService;
  private final PayorService payorService;
  private final DirectBillPlanRepository directBillPlanRepository;
  private final PartyClientXrefRepository partyClientXrefRepository;

  @Autowired
  public BillingAccountService(AssureService assureService, PayorService payorService,
      DirectBillPlanRepository directBillPlanRepository,
      PartyClientXrefRepository partyClientXrefRepository) {
    this.assureService = assureService;
    this.payorService = payorService;
    this.directBillPlanRepository = directBillPlanRepository;
    this.partyClientXrefRepository = partyClientXrefRepository;
  }



  public void createBillingAccount(AccountBillDTO request) {
    try {
      // Call Assure to see if the account exists
      System.out.println("Request is " + request.toString());
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

      DirectBillPlan plan = this.directBillPlanRepository.findDirectBillPlan(
          request.getPayorInfo().getPayorAddress().getStateProvCd(),
          derivePolicyPkgType(request.getPolicy().getSystemId()),
          request.getPolicy().getPolicyPrefix(), request.getPolicy().getPaymentPlanCd(),
          deriveCollectMeth(request.getAccountInfo().getMethodOfPayment()),
          request.getPolicy().getEffectiveDt(), request.getPolicy().getExpirationDt());

      if (plan != null) {
        AccountCreationRequest accountCreateRequest = new AccountCreationRequest();
        accountCreateRequest.setAccountNumber(request.getAccountInfo().getCurrentAccountNbr());
        accountCreateRequest.setType("DirectBill");
        accountCreateRequest.setBillingType(plan.getBilTypeCd());
        accountCreateRequest.setAccountPlan(plan.getBilClassCd());
        // accountCreateRequest.setAccountDueDate(request.getPolicy().getEffectiveDt());
        // accountCreateRequest.setBillThroughDate(request.getPolicy().getEffectiveDt());
        accountCreateRequest.setPresentmentMethod("Paper");
        accountCreateRequest.setPayorId(partyId);
        // accountCreateRequest.setPayorAddressId(addressRequest.getSequence());

        String newAccountId = this.assureService.createAccount(accountCreateRequest);

        if (!newAccountId.isBlank()) {
          PartyClientXref xref = new PartyClientXref();
          xref.setExternalRefId(compressedGuid);
          // xref.setPartyGuid(request.getPayorInfo().getPartyGuid());
          xref.setPartyRole("YourPartyRole");
          xref.setDxcRole("YourDxcRole");
          xref.setPostDtTs(new Date());
          this.partyClientXrefRepository.save(xref);
        }
      }


    } catch (Exception e) {
      // Handle Assure service exceptions
      System.err.println("Error in billing account creation: " + e.getMessage());
    }
  }

  private String deriveCollectMeth(String methodOfPayment) {
    return "DI".equals(methodOfPayment) ? "I" : "EFT";
  }

  private String derivePolicyPkgType(String systemId) {
    switch (systemId) {
      case "CBP":
        return "FF";
      case "CSU":
        return "CPP";
      default:
        return "XXX";
    }
  }

}
