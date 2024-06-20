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
import com.cinfin.bam.entity.DirectBillPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AssureService {

    private static final Logger logger = LoggerFactory.getLogger(AssureService.class);
    private static final String BASE_URL = "http://10.224.192.45:8080/billing-accounts/v1/billing/accounts";
    private static final String ACCOUNT_TYPE = "DirectBill"; // Move to constant
    private static final String PRESENTMENT_METHOD = "Paper"; // Move to constant

    private static AccountCreationRequest getAccountCreationRequest(AccountBillDTO request, DirectBillPlan plan, String partyId, String addressSequence) {
        AccountCreationRequest accountCreateRequest = new AccountCreationRequest();
        accountCreateRequest.setAccountNumber(request.getAccountInfo().getCurrentAccountNbr());
        accountCreateRequest.setType(ACCOUNT_TYPE);
        accountCreateRequest.setBillingType(plan.getBilTypeCd());
        accountCreateRequest.setAccountPlan(plan.getBilClassCd());
        accountCreateRequest.setAccountDueDate(request.getPolicy().getEffectiveDt().toString());
        accountCreateRequest.setBillThroughDate(request.getPolicy().getEffectiveDt().toString());
        accountCreateRequest.setPresentmentMethod(PRESENTMENT_METHOD);
        accountCreateRequest.setPayorId(partyId);
        accountCreateRequest.setPayorAddressId(addressSequence);
        return accountCreateRequest;
    }
    private final RestTemplate restTemplate;

    private final ApplicationPropertiesConfig config;

    @Autowired
    public AssureService(RestTemplate restTemplate, ApplicationPropertiesConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public Account callCheckAccountExistsService(String accountNumber) {
        try {
            HttpHeaders headers = BillingAccountManagementUtil.getHttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = this.config.getAssureBaseUrl() + "/billing-accounts/v1/billing/accounts?filters=accountNumber:" + accountNumber;
            ResponseEntity<AccountExistCheckResponse> response = this.restTemplate.exchange(
                    url, HttpMethod.GET, entity, AccountExistCheckResponse.class);

            logger.info("Response: {}", response);
            if (response.getStatusCode().is2xxSuccessful() && !response.getBody().getContent().isEmpty()) {
                return response.getBody().getContent().get(0);
            }
            return null;
        } catch (Exception e) {
            logger.error("Error occurred while checking if account exists for {} due to {}", accountNumber, e.getMessage(), e);
            return null;
        }
    }

    public String callCreateAccountService(AccountBillDTO request, DirectBillPlan plan, String partyId, String addressSequence) {
        try {
            AccountCreationRequest accountCreateRequest = getAccountCreationRequest(request, plan, partyId, addressSequence);
            logger.info("Request is {}", accountCreateRequest);

            HttpHeaders headers = BillingAccountManagementUtil.getHttpHeaders();
            HttpEntity<AccountCreationRequest> httpEntity = new HttpEntity<>(accountCreateRequest, headers);
            ResponseEntity<AccountCreationResponse> response = this.restTemplate.postForEntity(
                    BASE_URL, httpEntity, AccountCreationResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                logger.info("Account created with ID as {}", response.getBody().getAccountId());
                return response.getBody().getAccountId();
            }
            return null;
        } catch (Exception e) {
            logger.error("Error in createAccount due to {}", e.getMessage(), e);
            return null;
        }
    }
}

}
