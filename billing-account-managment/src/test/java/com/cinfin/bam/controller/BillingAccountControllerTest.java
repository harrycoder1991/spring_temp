package com.cinfin.bam.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.cinfin.bam.config.ApplicationPropertiesConfig;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.dto.requests.AccountInfo;
import com.cinfin.bam.service.BillingAccountService;

public class BillingAccountControllerTest {

  @InjectMocks
  private BillingAccountController billingAccountController;

  @Mock
  private BillingAccountService billingAccountService;

  @Mock
  private ApplicationPropertiesConfig config;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateBillingAccount() {
    when(this.config.getUrl()).thenReturn("http://mockurl.com");
    when(this.config.getEndpoint()).thenReturn("/mock-endpoint");
    AccountBillDTO request = new AccountBillDTO();
    AccountInfo ac = new AccountInfo();
    ac.setCurrentAccountNbr("3453453");
    request.setAccountInfo(ac);
    doNothing().when(this.billingAccountService).createBillingAccount(request);

    ResponseEntity<Object> response = this.billingAccountController.createBillingAccount(request);
    // Verify the response
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String expectedResponse =
        "{\"message\": \"Billing account created successfully\", \"requestData\": "
            + request.toString() + "}";
    assertEquals(expectedResponse, response.getBody().toString());
  }
}
