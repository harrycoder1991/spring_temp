package com.cinfin.bam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinfin.bam.service.BillingAccountService;

@RestController
@RequestMapping("/api")
public class BillingAccountController {

	private final BillingAccountService billingAccountService;

    public BillingAccountController(BillingAccountService billingAccountService) {
        this.billingAccountService = billingAccountService;
    }
	
	
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Billing Account API!";
    }
/*
    @PostMapping("/check-and-create")
    public ResponseEntity<String> checkAndCreateAccount(@RequestBody AccountInfoDTO accountInfoDTO) {
        String response = billingAccountService.checkAndCreateAccount(accountInfoDTO);
        return ResponseEntity.ok(response);
    }
  */  
    
    @PostMapping("/process")
    public String processRequest(@RequestBody String request) {
        // Process the incoming request
        return "Request processed successfully!";
    }
}
