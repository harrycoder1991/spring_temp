package com.cinfin.bam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.dto.requests.AccountCreationRequest;
import com.cinfin.bam.dto.responses.Account;
import com.cinfin.bam.dto.responses.PartySearchItem;
import com.cinfin.bam.dto.responses.PayorSearchResponse;

@Service
public class AssureService {

  private static final String BASE_URL =
      "http://10.224.192.45:8080/billing-accounts/v1/billing/accounts";

  @Value("${assure.service.url}")
  private String assureServiceUrl;

  @Value("${assure.service.user.id}")
  private String userId;

  @Value("${assure.service.request}")
  private String serviceRequest;

  private final RestTemplate restTemplate;


  @Autowired
  public AssureService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String checkAccountExists(String accountNumber) throws Exception {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.set("X-CSC-User-Id", this.userId);
      headers.set("X-Service-request", this.serviceRequest);
      HttpEntity<String> entity = new HttpEntity<>(headers);
      String url = this.assureServiceUrl + "/billing-accounts/v1/billing/accounts" + "?query="
          + "&filters=accountNumber:" + accountNumber;
      ResponseEntity<Account> response =
          this.restTemplate.exchange(url, HttpMethod.GET, entity, Account.class);

      if (response != null) {
        return response.getBody().getAccountId();
      }
      return null;


    } catch (Exception e) {
      throw new Exception("Error checking account existence", e);
    }

  }

  public String createAccount(AccountCreationRequest request) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<AccountCreationRequest> httpEntity = new HttpEntity<>(request, headers);

    ResponseEntity<String> response =
        this.restTemplate.postForEntity(BASE_URL, httpEntity, String.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      String responseBody = response.getBody();
      // Assuming responseBody contains JSON with an "accountId" field
      // Implement JSON parsing to extract accountId
      return responseBody.substring(responseBody.indexOf("accountId") + 11,
          responseBody.indexOf(",", responseBody.indexOf("accountId")) - 1);
    } else {
      throw new RuntimeException("Failed to create account");
    }
  }

  public PartySearchItem getPayor(AccountBillDTO request) throws Exception {
    try {
      // Construct the URL for searching payors
      String queryParam = request.getPayorInfo().getPayorType().equals("CO")
          ? request.getPayorInfo().getCompanyName()
          : request.getPayorInfo().getLastName();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.add("X-CSC-User-Id", "Jharve4");
      headers.add("X-Service-request", "False");

      HttpEntity<String> entity = new HttpEntity<>(headers);

      String url = this.assureServiceUrl + "/parties/v1/parties" + "?query=" + queryParam
          + "&filters=addressType:Billing";

      ResponseEntity<PayorSearchResponse> payorSearchResponse =
          this.restTemplate.exchange(url, HttpMethod.GET, entity, PayorSearchResponse.class);

      // Iterate through the list of payors to find the exact match
      for (PartySearchItem payor : payorSearchResponse.getBody().getEmbedded()
          .getPartySearchList()) {
        if (request.getPayorInfo().getPayorType().equals("CO")
            && payor.getName().equals(request.getPayorInfo().getCompanyName())) {
          return payor;
        } else if (request.getPayorInfo().getPayorType().equals("IN"))

        {
          String fullName = request.getPayorInfo().getFirstName()
              + request.getPayorInfo().getMiddleName() + request.getPayorInfo().getLastName();
          if (fullName.equalsIgnoreCase(payor.getName())) {
            return payor;
          }


        }
      }

      // If no exact match found, return null
      return null;

    } catch (HttpClientErrorException.NotFound e) {
      // If payor not found, return null
      return null;
    } catch (HttpClientErrorException | HttpServerErrorException e) {
      throw new Exception("Error retrieving payor information", e);
    }
  }
}
