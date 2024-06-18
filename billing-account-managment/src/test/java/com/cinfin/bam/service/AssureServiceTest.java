package com.cinfin.bam.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.cinfin.bam.config.ApplicationPropertiesConfig;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.dto.requests.AccountInfo;
import com.cinfin.bam.dto.responses.Account;
import com.cinfin.bam.dto.responses.AccountCreationResponse;
import com.cinfin.bam.entity.DirectBillPlan;
import com.cinfin.billing.accountmanagement.model.responses.AccountExistCheckResponse;


@ExtendWith(MockitoExtension.class)
public class AssureServiceTest {

  @InjectMocks
  private AssureService assureService;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private ApplicationPropertiesConfig config;

  @BeforeEach
  public void setUp() {
    // Mocking ApplicationPropertiesConfig
    when(this.config.getAssureBaseUrl()).thenReturn("http://mockserver");
  }

  @Test
  public void testCallCheckAccountExistsService_accountDoesNotExist() {
    String accountNumber = "123456";
    String url = "http://mockserver/billing-accounts/v1/billing/accounts?filters=accountNumber:"
        + accountNumber;

    AccountExistCheckResponse response = new AccountExistCheckResponse();
    response.setContent(Collections.emptyList());

    // Mocking RestTemplate's exchange method for GET request
    when(this.restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class),
        eq(AccountExistCheckResponse.class)))
            .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

    Account result = this.assureService.callCheckAccountExistsService(accountNumber);

    assertNull(result);
  }

  @Test
  public void testCallCheckAccountExistsService_accountExists() {
    String accountNumber = "123456";
    String url = "http://mockserver/billing-accounts/v1/billing/accounts?filters=accountNumber:"
        + accountNumber;

    Account account = new Account();
    account.setAccountId("account-id-123");
    AccountExistCheckResponse response = new AccountExistCheckResponse();
    response.setContent(Collections.singletonList(account));

    // Mocking RestTemplate's exchange method for GET request
    when(this.restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class),
        eq(AccountExistCheckResponse.class)))
            .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

    Account result = this.assureService.callCheckAccountExistsService(accountNumber);

    assertNotNull(result);
    assertEquals("account-id-123", result.getAccountId());
  }

  @Test
  public void testCallCreateAccountService_error() {
    AccountBillDTO request = new AccountBillDTO();
    AccountInfo accountInfo = new AccountInfo();
    accountInfo.setCurrentAccountNbr("123456");
    request.setAccountInfo(accountInfo);

    Policy policy = new Policy();
    policy.setEffectiveDt("2023-01-01");
    policy.setExpirationDt("2023-12-31");
    request.setPolicy(policy);

    DirectBillPlan plan = new DirectBillPlan();
    plan.setBilTypeCd("TypeCD");
    plan.setBilClassCd("ClassCD");

    String partyId = "party-id-123";
    String addressSequence = "address-seq-123";

    String url = "http://mockserver/billing-accounts/v1/billing/accounts";

    // Mocking RestTemplate's postForEntity method for POST request with an error response
    when(this.restTemplate.postForEntity(eq(url), any(HttpEntity.class),
        eq(AccountCreationResponse.class)))
            .thenReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));

    String result =
        this.assureService.callCreateAccountService(request, plan, partyId, addressSequence);

    assertNull(result);
  }

  @Test
  public void testCallCreateAccountService_successful() {
    AccountBillDTO request = new AccountBillDTO();
    AccountInfo accountInfo = new AccountInfo();
    accountInfo.setCurrentAccountNbr("123456");
    request.setAccountInfo(accountInfo);

    Policy policy = new Policy();
    policy.setEffectiveDt("2023-01-01");
    policy.setExpirationDt("2023-12-31");
    request.setPolicy(policy);

    DirectBillPlan plan = new DirectBillPlan();
    plan.setBilTypeCd("TypeCD");
    plan.setBilClassCd("ClassCD");

    String partyId = "party-id-123";
    String addressSequence = "address-seq-123";

    String url = "http://mockserver/billing-accounts/v1/billing/accounts";

    AccountCreationResponse accountCreationResponse = new AccountCreationResponse();
    accountCreationResponse.setAccountId("new-account-id");

    // Mocking RestTemplate's postForEntity method for POST request
    when(this.restTemplate.postForEntity(eq(url), any(HttpEntity.class),
        eq(AccountCreationResponse.class)))
            .thenReturn(new ResponseEntity<>(accountCreationResponse, HttpStatus.OK));

    String result =
        this.assureService.callCreateAccountService(request, plan, partyId, addressSequence);

    assertEquals("new-account-id", result);
  }
}


