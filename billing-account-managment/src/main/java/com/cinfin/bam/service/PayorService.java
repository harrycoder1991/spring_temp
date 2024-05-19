package com.cinfin.bam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.dto.requests.AdditionalNameRequest;
import com.cinfin.bam.dto.requests.AddressRequest;
import com.cinfin.bam.dto.requests.PayorRequest;

@Service
public class PayorService {

  @Value("${assure.api.createpayor.url}")
  private String assureCreatePayorServiceUrl;

  @Autowired
  private RestTemplate restTemplate;

  public PayorService(String assureCreatePayorServiceUrl, RestTemplate restTemplate) {
    this.assureCreatePayorServiceUrl = assureCreatePayorServiceUrl;
    this.restTemplate = restTemplate;
  }

  public void createAdditionalName(String partyId, AdditionalNameRequest additionalNameRequest) {
    String url = this.assureCreatePayorServiceUrl + "/" + partyId + "/additionalNames";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<AdditionalNameRequest> request = new HttpEntity<>(additionalNameRequest, headers);

    ResponseEntity<String> response = this.restTemplate.postForEntity(url, request, String.class);
    // sequence will be there in response
    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new RuntimeException("Failed to create additional name");
    }
  }

  public void createAddress(String partyId, AddressRequest addressRequest) {
    String url = this.assureCreatePayorServiceUrl + "/" + partyId + "/addresses";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<AddressRequest> request = new HttpEntity<>(addressRequest, headers);

    ResponseEntity<String> response = this.restTemplate.postForEntity(url, request, String.class);

    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new RuntimeException("Failed to create address");
    }
  }

  public AddressRequest createNewPayorAddressRequest(AccountBillDTO request) {
    AddressRequest addressRequest = new AddressRequest();
    addressRequest.setAddressType("Billing");
    if (request.getPayorInfo().getPayorAddress().getAddr2() != null) {
      addressRequest.setAddressLine2(request.getPayorInfo().getPayorAddress().getAddr2());
    }
    if (request.getPayorInfo().getPayorAddress().getAddr1() != null) {
      addressRequest.setAddressLine1(request.getPayorInfo().getPayorAddress().getAddr1());
    }
    /*
     * if (request.get.getAttentionLine() != null) {
     * addressRequest.setAttentionLine(input.getAttentionLine()); }
     */
    addressRequest.setCity(request.getPayorInfo().getPayorAddress().getCity());
    addressRequest.setPostal(request.getPayorInfo().getPayorAddress().getZip()
        + (request.getPayorInfo().getPayorAddress().getZipExtension() != null
            ? "-" + request.getPayorInfo().getPayorAddress().getZipExtension()
            : ""));
    addressRequest.setState(request.getPayorInfo().getPayorAddress().getStateProvCd());
    addressRequest.setCountry(request.getPayorInfo().getPayorAddress().getCountry());
    return addressRequest;
  }

  public String createPayor(PayorRequest payorRequest) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<PayorRequest> request = new HttpEntity<>(payorRequest, headers);

    ResponseEntity<String> response =
        this.restTemplate.postForEntity(this.assureCreatePayorServiceUrl, request, String.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      // Extract partyId from response
      String responseBody = response.getBody();
      // Assuming responseBody is a JSON string, parse to extract partyId
      // This could be improved with proper JSON parsing
      return responseBody.substring(responseBody.indexOf("partyId") + 10,
          responseBody.indexOf(",", responseBody.indexOf("partyId")) - 1);
    } else {
      throw new RuntimeException("Failed to create payor");
    }
  }

  public PayorRequest createPayorRequest(AccountBillDTO request, String compressedGuid) {
    PayorRequest payorRequest = new PayorRequest();

    if ("IN".equals(request.getPayorInfo().getPayorType())) {
      payorRequest.setPerson(true);

      PayorRequest.Details details = new PayorRequest.Details();
      PayorRequest.Details.Name name = new PayorRequest.Details.Name();
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
      payorRequest.setDetails(details);
    } else if ("CO".equals(request.getPayorInfo().getPayorType())) {
      payorRequest.setPerson(false);

      PayorRequest.Details details = new PayorRequest.Details();
      PayorRequest.Details.Name name = new PayorRequest.Details.Name();
      name.setGiven(request.getPayorInfo().getCompanyName());
      details.setName(name);
      payorRequest.setDetails(details);
    }

    PayorRequest.Email email = new PayorRequest.Email();
    email.setPrimary(request.getPayorInfo().getPayorEmail());
    payorRequest.setEmail(email);

    PayorRequest.Phone phone = new PayorRequest.Phone();
    PayorRequest.Phone.Primary primary = new PayorRequest.Phone.Primary();
    primary.setNumber(request.getPayorInfo().getPayorPhone());
    phone.setPrimary(primary);
    payorRequest.setPhone(phone);

    payorRequest.setExternalId(compressedGuid);

    return payorRequest;
  }
}


