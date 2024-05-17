package com.cinfin.bam.controller;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.service.BillingAccountService;

@RestController
@RequestMapping("/api")
public class BillingAccountController {

  private final BillingAccountService billingAccountService;

  public BillingAccountController(BillingAccountService billingAccountService) {
    this.billingAccountService = billingAccountService;
  }


  @PostMapping(value = "/createBillingAccount", consumes = MediaType.APPLICATION_XML_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createBillingAccount(@Valid @RequestBody AccountBillDTO request) {
    this.billingAccountService.createBillingAccount(request);
    String jsonResponse =
        "{\"message\": \"Billing account created successfully\", \"requestData\": "
            + request.toString() + "}";

    return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
  }


  @GetMapping("/hello")
  public String hello() {
    return "Hello from Billing Account API!";
  }
}
