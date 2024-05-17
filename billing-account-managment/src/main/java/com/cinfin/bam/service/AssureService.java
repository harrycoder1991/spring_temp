package com.cinfin.bam.service;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.dto.responses.Account;
import com.cinfin.bam.dto.responses.AccountResponse;
import com.cinfin.bam.dto.responses.PayorSearchResponse;
import com.cinfin.bam.dto.responses.PayorSearchResponse.PartySearchItem;

@Service
public class AssureService {

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
      ResponseEntity<AccountResponse> response =
          this.restTemplate.exchange(url, HttpMethod.GET, entity, AccountResponse.class);

      if (response != null && response.getEmbedded() != null
          && response.getEmbedded().getAccountSearchList() != null) {
        List<Account> accountSearchList = response.getEmbedded().getAccountSearchList();
        if (!accountSearchList.isEmpty()) {
          return accountSearchList.get(0).getAccountId();
        }
      }
      return null;
    } catch (HttpClientErrorException | HttpServerErrorException e) {
      throw new Exception("Error checking account existence", e);
    }
  }



  public PartySearchItem getPayor(AccountBillDTO request) throws Exception {
    try {
      // Construct the URL for searching payors
      String queryParam = request.getPayorInfo().getPayorType().equals("CO")
          ? request.getPayorInfo().getCompanyName()
          : request.getPayorInfo().getLastName();
      URI uri = UriComponentsBuilder.fromHttpUrl(this.apiBaseUrl).path("/parties/v1/parties")
          .queryParam("query", queryParam).queryParam("filters", "addressType:Billing").build()
          .toUri();

      // Make the GET request to search for payors
      PayorSearchResponse payorSearchResponse =
          this.restTemplate.getForObject(uri, PayorSearchResponse.class);

      // Iterate through the list of payors to find the exact match
      for (PartySearchItem payor : payorSearchResponse.getEmbedded().getPartySearchList()) {
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
