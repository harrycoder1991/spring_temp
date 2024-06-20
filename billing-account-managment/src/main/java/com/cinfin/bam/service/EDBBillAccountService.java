package com.cinfin.bam.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.dto.requests.AdditionalNameRequest;
import com.cinfin.bam.dto.responses.Account;
import com.cinfin.bam.entity.DirectBillPlan;
import com.cinfin.bam.entity.PartyClientXref;
import com.cinfin.bam.repository.DirectBillPlanRepository;
import com.cinfin.bam.repository.PartyClientXrefRepository;
import com.cinfin.bam.utils.BillingAccountManagementUtil;

@Service
public class EDBBillAccountService {
  private static final Logger logger = LoggerFactory.getLogger(EDBBillAccountService.class);

  private final AssureService assureService;
  private final PayorService payorService;
  private final DirectBillPlanRepository directBillPlanRepository;
  private final PartyClientXrefRepository partyClientXrefRepository;

  @Autowired
  public EDBBillAccountService(AssureService assureService, PayorService payorService,
      DirectBillPlanRepository directBillPlanRepository,
      PartyClientXrefRepository partyClientXrefRepository) {
    this.assureService = assureService;
    this.payorService = payorService;
    this.directBillPlanRepository = directBillPlanRepository;
    this.partyClientXrefRepository = partyClientXrefRepository;
  }

  private Optional<String> checkIfPayorExists(String partyGuid) {
    return this.partyClientXrefRepository.findByPartyGuid(partyGuid)
        .map(PartyClientXref::getExternalRefId);
  }

  private void createAccount(AccountBillDTO request, String partyId, String guid,
      String addressSequence) {
    String paymentPlan = BillingAccountManagementUtil.determinePaymentPlan(request);
    List<DirectBillPlan> plans = this.directBillPlanRepository.findDirectBillPlan(
        request.getPayorInfo().getPayorAddress().getStateProvCd(),
        derivePolicyPkgType(request.getPolicy().getSystemId()),
        request.getPolicy().getPolicyPrefix(), paymentPlan,
        deriveCollectMeth(request.getAccountInfo().getMethodOfPayment()),
        request.getPolicy().getEffectiveDt(), request.getPolicy().getExpirationDt());

    if (!plans.isEmpty()) {
      String newAccountId = this.assureService.callCreateAccountService(request, plans.get(0),
          partyId, addressSequence);
      if (StringUtils.isNotBlank(newAccountId)) {
        PartyClientXref xref = new PartyClientXref();
        xref.setExternalRefId(partyId);
        xref.setPartyGuid(guid);
        xref.setPartyRole("Payor");
        xref.setPostDtTs(LocalDateTime.now());
        this.partyClientXrefRepository.save(xref);
      }
    }
  }

  public void createBillingAccount(AccountBillDTO request) {
    try {
      if (StringUtils.equalsIgnoreCase(request.getAccountInfo().getMethodOfPayment(), "DI")) {
        logger.info("Input request is " + request);

        String partyId = "Not Available";
        String guid = request.getPayorInfo().getPartyGuid();
        String guidBase64 = BillingAccountManagementUtil.encodeUUIDToBase64(guid);
        logger.info("Encoded GUID is " + guidBase64);

        Account existingAccountDetails = this.assureService
            .callCheckAccountExistsService(request.getAccountInfo().getCurrentAccountNbr());
        if (existingAccountDetails == null) { // Account doesn't exist
          // Payor creation logic
          String addressSequence = "1";
          Optional<String> existingPayor = checkIfPayorExists(guid);
          if (existingPayor.isPresent()) {
            logger.info("Existing payor found with id as " + existingPayor.get());
            partyId = existingPayor.get();
          } else {
            // Create new payor
            logger.info("Existing payor not found, hence creating new");
            partyId = this.payorService.callCreatePayorService(request, guidBase64);
            if (partyId != null) {
              if (request.getPayorInfo().getSecondaryName() != null) {
                AdditionalNameRequest additionalNameRequest = new AdditionalNameRequest();
                additionalNameRequest.setType("Doing Business As");
                additionalNameRequest.setFullName(request.getPayorInfo().getSecondaryName());
                this.payorService.callCreateAdditionalNameService(partyId, additionalNameRequest);
              }
              addressSequence = this.payorService.callCreateAddressService(request, partyId);
              createAccount(request, partyId, guid, addressSequence);
            }
          }
        } else {
          createAccount(request, partyId, guid, addressSequence);
          logger.info("Account already exists: " + existingAccountDetails.getAccountId());
        }
      } else {
        logger.info("Method of Payment is not DI, hence skipping payor and account creation");
      }
    } catch (Exception e) {
      logger.error("Error in billing account creation: " + e.getMessage(), e);
    }
  }

  private String deriveCollectMeth(String methodOfPayment) {
    return "DI".equalsIgnoreCase(methodOfPayment) ? "I" : "EFT";
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
