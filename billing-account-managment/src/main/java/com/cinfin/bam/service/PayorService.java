package com.cinfin.bam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.cinfin.bam.config.ApplicationPropertiesConfig;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.dto.requests.AdditionalNameRequest;
import com.cinfin.bam.dto.requests.AddressRequest;
import com.cinfin.bam.dto.requests.PayorRequest;
import com.cinfin.bam.utils.BillingAccountManagementUtil;

@Service
public class PayorService {

  private static final Logger logger = LoggerFactory.getLogger(PayorService.class);

  private final ApplicationPropertiesConfig config;
  private final RestTemplate restTemplate;

  @Autowired
  public PayorService(RestTemplate restTemplate, ApplicationPropertiesConfig config) {
    this.restTemplate = restTemplate;
    this.config = config;
  }

  public void callCreateAdditionalNameService(String partyId,
      AdditionalNameRequest additionalNameRequest) {
    try {
      String url = this.config.getAssureBaseUrl() + this.config.getAssureCreatePayorEndpoint() + "/"
          + partyId + "/additionalNames";
      HttpHeaders headers = BillingAccountManagementUtil.getHttpHeaders();
      HttpEntity<AdditionalNameRequest> request = new HttpEntity<>(additionalNameRequest, headers);
      ResponseEntity<PayorAdditionalNameCreateResponse> response =
          this.restTemplate.postForEntity(url, request, PayorAdditionalNameCreateResponse.class);
      logger.info("PayorAdditionalNameCreateResponse received with sequence as "
          + response.getBody().getSequence());

      if (!response.getStatusCode().is2xxSuccessful()) {
        throw new RuntimeException("Failed to create additional name");
      }
    } catch (Exception e) {
      logger.error("Error occurred in callCreateAdditionalNameService due to " + e.getMessage(), e);
    }
  }

  public String callCreateAddressService(AccountBillDTO inputRequest, String partyId) {
    try {
      AddressRequest addressRequest = createNewPayorAddressRequest(inputRequest);
      String url = this.config.getAssureBaseUrl() + this.config.getAssureCreatePayorEndpoint() + "/"
          + partyId + "/addresses";
      HttpHeaders headers = BillingAccountManagementUtil.getHttpHeaders();
      HttpEntity<AddressRequest> request = new HttpEntity<>(addressRequest, headers);
      ResponseEntity<CreateAddressResponse> response =
          this.restTemplate.postForEntity(url, request, CreateAddressResponse.class);
      logger.info("CreateAddressResponse received with Address sequence as "
          + response.getBody().getAddressSequence());

      if (!response.getStatusCode().is2xxSuccessful()) {
        throw new RuntimeException("Failed to create address");
      }
      return response.getBody().getAddressSequence();
    } catch (Exception e) {
      logger.error("Error occurred in callCreateAddressService due to " + e.getMessage(), e);
      return null;
    }
  }

  public String callCreatePayorService(AccountBillDTO inputRequest, String compressedGuid) {
    try {
      logger.info("Inside callCreatePayorService");
      PayorRequest payorRequest = createPayorRequest(inputRequest, compressedGuid);
      HttpHeaders headers = BillingAccountManagementUtil.getHttpHeaders();
      HttpEntity<PayorRequest> request = new HttpEntity<>(payorRequest, headers);
      String url = this.config.getAssureBaseUrl() + this.config.getAssureCreatePayorEndpoint();
      ResponseEntity<PayorCreationResponse> response =
          this.restTemplate.postForEntity(url, request, PayorCreationResponse.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        String partyId = response.getBody().getPartyId();
        logger.info("Party Id is " + partyId);

        if (isDummyEmail(payorRequest.getEmail().getPrimary())) {
          callPayorInfoUpdateService(partyId);
        }

        return partyId;
      } else {
        logger.error("Error occurred in callCreatePayorService due to " + response.getStatusCode());
        throw new RuntimeException("Failed to create payor");
      }
    } catch (Exception e) {
      logger.error("Error occurred in callCreatePayorService due to " + e.getMessage(), e);
      return null;
    }
  }

  public void callPayorInfoUpdateService(String payorId) {
    try {
      UpdatePayorRequestDTO request = new UpdatePayorRequestDTO();
      logger.info("**Request is " + request.toString());
      HttpHeaders headers = BillingAccountManagementUtil.getHttpHeaders();
      HttpEntity<UpdatePayorRequestDTO> httpEntity = new HttpEntity<>(request, headers);
      String url = "http://10.224.192.45:8080/parties/v1/parties/" + payorId;
      ResponseEntity<String> response =
          this.restTemplate.exchange(url, HttpMethod.PATCH, httpEntity, String.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        logger.info("Payor updated by removing dummy values for PayorId: " + payorId);
      } else {
        logger.info("Failed to update Payor by removing dummy values for PayorId: " + payorId);
      }
    } catch (Exception e) {
      logger.error("Error in callPayorInfoUpdateService due to " + e.getMessage(), e);
    }
  }

  private AddressRequest createNewPayorAddressRequest(AccountBillDTO request) {
    AddressRequest addressRequest = new AddressRequest();
    addressRequest.setAddressType("Billing");
    if (request.getPayorInfo().getPayorAddress().getAddr2() != null) {
      addressRequest.setAddressLine2(request.getPayorInfo().getPayorAddress().getAddr2());
    }
    if (request.getPayorInfo().getPayorAddress().getAddr1() != null) {
      addressRequest.setAddressLine1(request.getPayorInfo().getPayorAddress().getAddr1());
    }
    addressRequest.setCity(request.getPayorInfo().getPayorAddress().getCity());
    addressRequest.setPostal(request.getPayorInfo().getPayorAddress().getZip()
        + (request.getPayorInfo().getPayorAddress().getZipExtension() != null
            ? "-" + request.getPayorInfo().getPayorAddress().getZipExtension()
            : ""));
    addressRequest.setState(request.getPayorInfo().getPayorAddress().getStateProvCd());
    addressRequest.setCountry(request.getPayorInfo().getPayorAddress().getCountry());
    return addressRequest;
  }

  private PayorRequest createPayorRequest(AccountBillDTO request, String compressedGuid) {
    PayorRequest payorRequest = new PayorRequest();
    PayorRequest.Details details = new PayorRequest.Details();
    PayorRequest.Details.Name name = new PayorRequest.Details.Name();

    details.setExternalId(compressedGuid);
    if ("IN".equals(request.getPayorInfo().getPayorType())) {
      payorRequest.setPerson(true);
      name.setGiven(request.getPayorInfo().getFirstName());
      if (request.getPayorInfo().getMiddleName() != null) {
        name.setMiddle(request.getPayorInfo().getMiddleName());
      }
      name.setFamily(request.getPayorInfo().getLastName());
      if (request.getPayorInfo().getTitlePrefix() != null) {
        name.setPrefix(request.getPayorInfo().getTitlePrefix());
      }
      if (request.getPayorInfo().getNameSuffix() != null) {
        name.setSuffix(request.getPayorInfo().getNameSuffix());
      }
      details.setName(name);
    } else if ("CO".equals(request.getPayorInfo().getPayorType())) {
      payorRequest.setPerson(false);
      name.setGiven(request.getPayorInfo().getCompanyName());
      details.setName(name);
    }

    payorRequest.setDetails(details);

    PayorRequest.Email email = new PayorRequest.Email();
    email.setPrimary(request.getPayorInfo().getPayorEmail());
    payorRequest.setEmail(email);

    PayorRequest.Phone phone = new PayorRequest.Phone();
    PayorRequest.Phone.Primary primary = new PayorRequest.Phone.Primary();
    primary.setNumber(request.getPayorInfo().getPayorPhone());
    phone.setPrimary(primary);
    payorRequest.setPhone(phone);

    return payorRequest;
  }

  private boolean isDummyEmail(String email) {
    return "no-reply@cinfin.com".equalsIgnoreCase(email);
  }
}
